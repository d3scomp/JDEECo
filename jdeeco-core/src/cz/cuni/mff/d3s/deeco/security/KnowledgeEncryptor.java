package cz.cuni.mff.d3s.deeco.security;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.ShortBufferException;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeMetaData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeSecurityAnnotation;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;

/**
 * @author Ondřej Štumpf  
 */
public class KnowledgeEncryptor {
	
	private final SecurityKeyManager keyManager;
	private final SecurityHelper securityHelper;
	private final RemoteSecurityChecker securityChecker;
	
	public KnowledgeEncryptor(SecurityKeyManager keyManager) {
		this.keyManager = keyManager;
		this.securityHelper = new SecurityHelper();
		this.securityChecker = new RemoteSecurityChecker();
	}
	
	@SuppressWarnings("unchecked")
	public KnowledgeData decryptValueSet(KnowledgeData kd, KnowledgeManager replica, KnowledgeMetaData metaData) {
		if (kd == null) return null;
		
		ValueSet decryptedKnowledge = decrypt(kd.getKnowledge(), replica, metaData);	
		ValueSet decryptedSecuritySet = decrypt(kd.getSecuritySet(), replica, metaData);
		
		// replace lists of serializable roles with actual security tags
		for (KnowledgePath path : decryptedSecuritySet.getKnowledgePaths()) {
			List<KnowledgeSecurityTag> tags = (List<KnowledgeSecurityTag>)decryptedSecuritySet.getValue(path);			
			decryptedSecuritySet.setValue(path, tags);
		}
		
		return new KnowledgeData(decryptedKnowledge, decryptedSecuritySet, metaData);
	}
	
	private ValueSet decrypt(ValueSet valueSet, KnowledgeManager replica, KnowledgeMetaData metaData) {
		ValueSet result = new ValueSet();
		for (KnowledgePath kp : valueSet.getKnowledgePaths()) {
			Object value = valueSet.getValue(kp);
			if (value instanceof SealedObject) {
				try {
					Object decryptedValue = accessValue((SealedObject)value, replica, metaData);
					result.setValue(kp, decryptedValue);					
				} catch (KnowledgeNotFoundException | SecurityException e) {
					// do nothing
				}
			} else {
				result.setValue(kp, value);
			}
		}	
		
		return result;
	}

	public List<KnowledgeData> encryptValueSet(ValueSet valueSet, KnowledgeManager knowledgeManager, KnowledgeMetaData metaData) throws KnowledgeNotFoundException {
		if (valueSet == null) return null;
		
		Map<KnowledgeSecurityTag, ValueSet> securityMap = new HashMap<>();
		Map<KnowledgePath, List<KnowledgeSecurityTag>> securityTagsMap = new HashMap<>();
		
		// split the knowledge into groups according to their security
		for (KnowledgePath kp : valueSet.getKnowledgePaths()) {
			if (!KnowledgePathHelper.isAbsolutePath(kp)) {
				throw new IllegalArgumentException("The value set must contain only absolute knowledge paths.");
			}
			
			List<KnowledgeSecurityTag> tags = knowledgeManager.getKnowledgeSecurityTags((PathNodeField)kp.getNodes().get(0)).stream()
					.filter(t -> t instanceof KnowledgeSecurityTag).map(t -> (KnowledgeSecurityTag)t ).collect(Collectors.toList());
			securityTagsMap.put(kp, tags);
			
			if (tags == null || tags.isEmpty()) {
				addToSecurityMap(valueSet, securityMap, null, kp);				
			} else {
				for (KnowledgeSecurityTag tag : tags) {
					addToSecurityMap(valueSet, securityMap, tag, kp);
				}
				
			}
		}
		
		// create copies of the knowledge data, each encrypted for target role		
		List<KnowledgeData> result = new LinkedList<>();
		
		for (KnowledgeSecurityTag tag : securityMap.keySet()) {
			// clone the meta data so the the copy can be modified
			KnowledgeMetaData clonedMetaData = metaData.clone();
			
			if (tag == null) {
				// data not encrypted
				ValueSet vs = securityMap.get(null);
				result.add(new KnowledgeData(vs, new ValueSet(), clonedMetaData));
			} else {
				ValueSet vs = securityMap.get(tag);
				
				// associate each knowledge path with a list of security tags
				ValueSet securitySet = new ValueSet();
				for (KnowledgePath path : vs.getKnowledgePaths()) {
					securitySet.setValue(path, securityTagsMap.get(path) );
				}				
				
				Cipher cipher = prepareCipher(knowledgeManager, tag, clonedMetaData);
				seal(vs, cipher);
				seal(securitySet, cipher);
				result.add(new KnowledgeData(vs, securitySet, clonedMetaData));
			}
		}
		return result;
	}
	
	private Object accessValue(SealedObject sealedObject, KnowledgeManager replica, KnowledgeMetaData metaData) throws KnowledgeNotFoundException {
		// verify signature on metadata
		boolean verificationSucceeded = false;
		try {
			verificationSucceeded = securityHelper.verify(metaData.signature, keyManager.getIntegrityPublicKey(), metaData.componentId, metaData.versionId, metaData.targetRole);
		} catch (InvalidKeyException | CertificateEncodingException
				| SignatureException | NoSuchAlgorithmException
				| KeyStoreException | SecurityException | IllegalStateException e1) { }
		
		if (!verificationSucceeded) {
			throw new SecurityException();
		}
		
		Object value = null;
		boolean encryptionSucceeded = false;
		
		List<SecurityRole> transitiveRoles = RoleHelper.getTransitiveRoles(replica.getComponent().getRoles());
		
		for (SecurityRole role : transitiveRoles) {
			if (!securityChecker.checkSecurity(role, metaData.targetRole, replica.getComponent().getKnowledgeManager())) {
				continue;
			}
			
			String roleName = metaData.targetRole.getRoleName();
			Map<String, Object> arguments = metaData.targetRole.getRoleArguments();
			
			try {
				Key privateKey = keyManager.getPrivateKeyFor(roleName, arguments);				
				Key decryptedSymmetricKey = securityHelper.decryptKey(metaData.encryptedKey, metaData.encryptedKeyAlgorithm, privateKey);
				value = sealedObject.getObject(securityHelper.getSymmetricCipher(Cipher.DECRYPT_MODE, decryptedSymmetricKey));
				
				encryptionSucceeded = true;				
				break; // decryption succeeded - we have a value
			} catch (InvalidKeyException | ClassNotFoundException
					| IllegalBlockSizeException | BadPaddingException
					| NoSuchAlgorithmException | NoSuchPaddingException
					| IOException | ShortBufferException | CertificateEncodingException | KeyStoreException | SecurityException 
					| SignatureException | IllegalStateException e) { }		
		}
		
		if (!encryptionSucceeded) {
			throw new SecurityException();
		}
		return value;
	}

	private void addToSecurityMap(ValueSet basicValueSet,
			Map<KnowledgeSecurityTag, ValueSet> securityMap,
			KnowledgeSecurityTag tag, KnowledgePath kp) {
		if (!securityMap.containsKey(tag)) {
			securityMap.put(tag, new ValueSet());
		}
		securityMap.get(tag).setValue(kp, basicValueSet.getValue(kp));
	}
	
	private Cipher prepareCipher(KnowledgeManager knowledgeManager, KnowledgeSecurityTag tag, KnowledgeMetaData metaData) {
		try {
			String roleName = tag.getRequiredRole().getRoleName();
			Map<String, Object> arguments = RoleHelper.readRoleArguments(tag.getRequiredRole(), knowledgeManager);
			
			Key publicKey = keyManager.getPublicKeyFor(roleName, arguments);			
			Key symmetricKey = securityHelper.generateKey();
			
			byte[] encryptedKey = securityHelper.encryptKey(symmetricKey, publicKey);
			
			metaData.encryptedKey = encryptedKey;
			metaData.encryptedKeyAlgorithm = symmetricKey.getAlgorithm();
			metaData.targetRole = new KnowledgeSecurityAnnotation(roleName, arguments);
			metaData.signature = securityHelper.sign(keyManager.getIntegrityPrivateKey(), metaData.componentId, metaData.versionId, metaData.targetRole);
			
			return securityHelper.getSymmetricCipher(Cipher.ENCRYPT_MODE, symmetricKey);
		} catch (InvalidKeyException | CertificateEncodingException
				| KeyStoreException | NoSuchAlgorithmException
				| SecurityException | SignatureException
				| IllegalStateException | KnowledgeNotFoundException | NoSuchPaddingException 
				| ShortBufferException | IllegalBlockSizeException 
				| BadPaddingException | IOException e) {
			throw new SecurityException(e);
		}
	}
	
	private void seal(ValueSet values, Cipher cipher) throws KnowledgeNotFoundException {				
		try {						
			for (KnowledgePath kp : values.getKnowledgePaths()) {
				Object plainKnowledge = values.getValue(kp);			
				SealedObject encryptedKnowledge = new SealedObject((Serializable) plainKnowledge, cipher);				
				values.setValue(kp, encryptedKnowledge);						
			}		
		} catch (IllegalBlockSizeException | IOException | SecurityException | IllegalStateException e) {
			throw new SecurityException(e);
		}			
	}

	
}
