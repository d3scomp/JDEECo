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

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeMetaData;

/**
 * @author Ondřej Štumpf  
 */
public class KnowledgeEncryptor {
	
	private final SecurityKeyManager keyManager;
	private final SecurityHelper securityHelper;
	
	public KnowledgeEncryptor(SecurityKeyManager keyManager) {
		this.keyManager = keyManager;
		this.securityHelper = new SecurityHelper();
	}
	
	public void decryptChangeSet(ChangeSet changeSet, KnowledgeManager replica, KnowledgeMetaData metaData) {
		if (changeSet == null) return;
		
		for (KnowledgePath kp : changeSet.getUpdatedReferences()) {
			Object value = changeSet.getValue(kp);
			if (value instanceof SealedObject) {
				try {
					Object decryptedValue = decryptValue((SealedObject)value, replica, metaData);
					changeSet.setValue(kp, decryptedValue);
				} catch (KnowledgeNotFoundException | SecurityException e) {
					changeSet.remove(kp);
				}
			}
		}		

	}
	
	public List<KnowledgeData> encryptValueSet(ValueSet basicValueSet, KnowledgeManager km, KnowledgeMetaData metaData) throws KnowledgeNotFoundException {
		if (basicValueSet == null) return null;
		
		Map<KnowledgeSecurityTag, ValueSet> securityMap = new HashMap<>();
		
		// split the knowledge into groups according to their security
		for (KnowledgePath kp : basicValueSet.getKnowledgePaths()) {
			List<KnowledgeSecurityTag> tags = km.getSecurityTagsFor(kp);
			
			if (tags == null || tags.isEmpty()) {
				addToSecurityMap(basicValueSet, securityMap, null, kp);
			} else {
				for (KnowledgeSecurityTag tag : tags) {
					addToSecurityMap(basicValueSet, securityMap, tag, kp);
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
				result.add(new KnowledgeData(securityMap.get(null), clonedMetaData));
			} else {
				ValueSet vs = securityMap.get(tag);
				sealKnowledge(km, vs, tag, clonedMetaData);
				result.add(new KnowledgeData(vs, clonedMetaData));
			}
		}
		return result;
	}
	
	private Object decryptValue(SealedObject sealedObject, KnowledgeManager replica, KnowledgeMetaData metaData) throws KnowledgeNotFoundException {
		KnowledgeManager localKnowledgeManager = replica.getComponent().getKnowledgeManager();
		Object value = null;
		boolean encryptionSucceeded = false;
		
		for (SecurityRole role : replica.getComponent().getRoles()) {
			String roleName = role.getRoleName();
			ValueSet argumentsValueSet = localKnowledgeManager.get(role.getArguments());	
			
			List<Object> arguments = role.getArguments().stream().map(path -> argumentsValueSet.getValue(path)).collect(Collectors.toList());					
			
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
					| SignatureException | IllegalStateException e) {
			
			}		
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
	
	private void sealKnowledge(KnowledgeManager km, ValueSet valueSet, KnowledgeSecurityTag tag, KnowledgeMetaData metaData) throws KnowledgeNotFoundException {
		for (KnowledgePath kp : valueSet.getKnowledgePaths()) {
			Object plainKnowledge = valueSet.getValue(kp);
			String roleName = tag.getRoleName();
			
			ValueSet argumentsValueSet = km.get(tag.getArguments());			
			List<Object> arguments = tag.getArguments().stream().map(path -> argumentsValueSet.getValue(path)).collect(Collectors.toList());
						
			try {
				Key publicKey = keyManager.getPublicKeyFor(roleName, arguments);
				Key symmetricKey = securityHelper.generateKey();
				
				SealedObject encryptedKnowledge = new SealedObject((Serializable) plainKnowledge, securityHelper.getSymmetricCipher(Cipher.ENCRYPT_MODE, symmetricKey));
				byte[] encryptedKey = securityHelper.encryptKey(symmetricKey, publicKey);
				
				metaData.encryptedKey = encryptedKey;
				metaData.encryptedKeyAlgorithm = symmetricKey.getAlgorithm();
				metaData.targetRole = keyManager.getRoleKey(roleName, arguments);
				
				valueSet.setValue(kp, encryptedKnowledge);
			} catch (IllegalBlockSizeException | IOException | InvalidKeyException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException 
					| KeyStoreException | CertificateEncodingException | SecurityException | SignatureException | IllegalStateException | ShortBufferException e) {
				throw new SecurityException(e);
			}				
		}
		
	}
}
