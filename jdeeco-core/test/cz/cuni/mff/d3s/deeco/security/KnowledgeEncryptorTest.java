package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.ShortBufferException;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AccessRights;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.WildcardSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.WildcardSecurityTagExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeMetaData;

/**
 * @author Ondřej Štumpf  
 */
public class KnowledgeEncryptorTest {

	private KnowledgeEncryptor target;
	private BaseKnowledgeManager localKnowledgeManager, replicaKnowledgeManager;
	private ValueSet valueSet;
	private KnowledgeMetaData metaData;
	private RuntimeMetadataFactory factory;
	private SecurityHelper securityHelper;
	
	private PublicKey testrole1PublicKey, testrole2PublicKey;
	private PrivateKey testrole1PrivateKey;
	
	private SecurityKeyManager keyManagerMock;
	
	@Before
	public void setUp() throws InvalidKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, SecurityException, SignatureException, IllegalStateException, KnowledgeUpdateException {
		factory = RuntimeMetadataFactoryExt.eINSTANCE;
		
		valueSet = new ValueSet();
		valueSet.setValue(RuntimeModelHelper.createKnowledgePath("b"), "TEST");
		valueSet.setValue(RuntimeModelHelper.createKnowledgePath("c"), 123);
		valueSet.setValue(RuntimeModelHelper.createKnowledgePath("secured"), 666);
		valueSet.setValue(RuntimeModelHelper.createKnowledgePath("secured2"), 667);
		
		metaData = new KnowledgeMetaData("xy", 13, "A", 123456, 45);
		ComponentInstance component = mock(ComponentInstance.class);
		
		localKnowledgeManager = new BaseKnowledgeManager("sender_id", component);
		
		ChangeSet changeSet = new ChangeSet();
		changeSet.setValue(RuntimeModelHelper.createKnowledgePath("secured"), "secured_value");
		localKnowledgeManager.update(changeSet, "author_secured");
		
		when(component.getKnowledgeManager()).thenReturn(localKnowledgeManager);
	 	SecurityRole role = factory.createSecurityRole();
	 	role.setRoleName("testrole1");
	 	ComponentInstance tmpComponent = factory.createComponentInstance();
	 	tmpComponent.getSecurityRoles().add(role);
		when(component.getSecurityRoles()).thenReturn(tmpComponent.getSecurityRoles());
		
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("b"), Arrays.asList(createAllowEveryoneTag()));
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("c"), Arrays.asList(createAllowEveryoneTag()));
		
		replicaKnowledgeManager = new BaseKnowledgeManager("receiver_id", component);
		securityHelper = new SecurityHelper();
		
		KeyPair role1Pair = securityHelper.generateKeyPair("RSA", 1024);
		KeyPair role2Pair = securityHelper.generateKeyPair("RSA", 1024);
		KeyPair integrityPair = securityHelper.generateKeyPair();
		testrole1PublicKey = role1Pair.getPublic();
		testrole2PublicKey = role2Pair.getPublic();
		testrole1PrivateKey = role1Pair.getPrivate();
		
		keyManagerMock = spy(new SecurityKeyManagerImpl());
		when(keyManagerMock.getPublicKey(eq("testrole1"), anyObject())).thenReturn(testrole1PublicKey);		
		when(keyManagerMock.getPrivateKey(eq("testrole1"), anyObject())).thenReturn(testrole1PrivateKey);
		when(keyManagerMock.getPublicKey(eq("testrole2"), anyObject())).thenReturn(testrole2PublicKey);
		when(keyManagerMock.getIntegrityPublicKey()).thenReturn(integrityPair.getPublic());
		when(keyManagerMock.getIntegrityPrivateKey()).thenReturn(integrityPair.getPrivate());
		
		target = new KnowledgeEncryptor(keyManagerMock);
	}
	
	private WildcardSecurityTag createAllowEveryoneTag() {
		WildcardSecurityTag tag = factory.createWildcardSecurityTag();
		tag.setAccessRights(AccessRights.READ_WRITE);		
		return tag;
	}
	
	@Test
	public void encryptValueSet_NullTest() throws KnowledgeNotFoundException {
		// given null is passed
		
		// when encryptValueSet() is called
		List<KnowledgeData> result = target.encryptValueSet(null, localKnowledgeManager, metaData);
		
		assertNull(result);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void encryptValueSet_NonAbsoluteTest() throws KnowledgeNotFoundException {
		// given value set contains non absolute path
		KnowledgePath nonAbsolutePath = RuntimeModelHelper.createKnowledgePath("map");
		KnowledgePath innerPath = RuntimeModelHelper.createKnowledgePath("key");
		PathNodeMapKey mapKey = factory.createPathNodeMapKey();
		mapKey.setKeyPath(innerPath);
		nonAbsolutePath.getNodes().add(mapKey);
		
		valueSet.setValue(nonAbsolutePath, 987);
		
		// when encryptValueSet() is called
		target.encryptValueSet(valueSet, localKnowledgeManager, metaData);
		
		// then exception is thrown
	}
	
	@Test
	public void encryptValueSet_NoSecurityTestWithPlaintextSignEnabled() throws KnowledgeNotFoundException {
		// given no security is used
		System.setProperty(DeecoProperties.SIGN_PLAINTEXT_MESSAGES, "true");
		
		// re-create the target to accepted the new system property value
		target = new KnowledgeEncryptor(keyManagerMock);
		
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("secured"), Arrays.asList(createAllowEveryoneTag()));
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("secured2"), Arrays.asList(createAllowEveryoneTag()));
		
		// when encryptValueSet() is called
		List<KnowledgeData> result = target.encryptValueSet(valueSet, localKnowledgeManager, metaData);
		
		// then data are not encrypted and not modified
		assertEquals(1, result.size());
		assertEquals(metaData, result.get(0).getMetaData());
		assertEquals(valueSet, result.get(0).getKnowledge());
		
		assertEquals(4, result.get(0).getAuthors().getKnowledgePaths().size());
		assertEquals("author_secured", result.get(0).getAuthors().getValue(RuntimeModelHelper.createKnowledgePath("secured")));
		assertNull(result.get(0).getAuthors().getValue(RuntimeModelHelper.createKnowledgePath("secured2")));
		
		for (KnowledgePath kp : result.get(0).getKnowledge().getKnowledgePaths()) {
			assertFalse(result.get(0).getKnowledge().getValue(kp) instanceof SealedObject);
		}
		for (KnowledgePath kp : result.get(0).getSecuritySet().getKnowledgePaths()) {
			assertFalse(result.get(0).getSecuritySet().getValue(kp) instanceof SealedObject);
		}
		
		assertNull(result.get(0).getMetaData().encryptedKey);
		assertNotNull(result.get(0).getMetaData().signature);
	}
	
	@Test
	public void encryptValueSet_NoSecurityTestWithPlaintextSignDisabled() throws KnowledgeNotFoundException {
		// given no security is used
		System.setProperty(DeecoProperties.SIGN_PLAINTEXT_MESSAGES, "false");
		
		// re-create the target to accepted the new system property value
		target = new KnowledgeEncryptor(keyManagerMock);
		
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("secured"), Arrays.asList(createAllowEveryoneTag()));
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("secured2"), Arrays.asList(createAllowEveryoneTag()));
		
		// when encryptValueSet() is called
		List<KnowledgeData> result = target.encryptValueSet(valueSet, localKnowledgeManager, metaData);
		
		// then data are not encrypted and not modified
		assertEquals(1, result.size());
		assertEquals(metaData, result.get(0).getMetaData());
		assertEquals(valueSet, result.get(0).getKnowledge());
		
		assertEquals(valueSet.getKnowledgePaths().size(), result.get(0).getSecuritySet().getKnowledgePaths().size());
		for (KnowledgePath kp : result.get(0).getSecuritySet().getKnowledgePaths()) {
			@SuppressWarnings("unchecked")
			List<WildcardSecurityTag> tags = (List<WildcardSecurityTag>)result.get(0).getSecuritySet().getValue(kp);
			tags.stream().forEach(tag -> assertTrue(tag.getClass().equals(WildcardSecurityTagExt.class)));
		}
		
		assertEquals(4, result.get(0).getAuthors().getKnowledgePaths().size());
		assertEquals("author_secured", result.get(0).getAuthors().getValue(RuntimeModelHelper.createKnowledgePath("secured")));
		assertNull(result.get(0).getAuthors().getValue(RuntimeModelHelper.createKnowledgePath("secured2")));
		
		for (KnowledgePath kp : result.get(0).getKnowledge().getKnowledgePaths()) {
			assertFalse(result.get(0).getKnowledge().getValue(kp) instanceof SealedObject);
		}
		for (KnowledgePath kp : result.get(0).getSecuritySet().getKnowledgePaths()) {
			assertFalse(result.get(0).getSecuritySet().getValue(kp) instanceof SealedObject);
		}
		
		assertNull(result.get(0).getMetaData().encryptedKey);
		assertNull(result.get(0).getMetaData().signature);
	}
	
	@Test
	public void encryptValueSet_SecurityTest() throws KnowledgeNotFoundException {		
		KnowledgeSecurityTag tag1 = factory.createKnowledgeSecurityTag();
		tag1.setRequiredRole(factory.createSecurityRole());
		tag1.getRequiredRole().setRoleName("testrole1");
		KnowledgeSecurityTag tag2 = factory.createKnowledgeSecurityTag();
		tag2.setRequiredRole(factory.createSecurityRole());
		tag2.getRequiredRole().setRoleName("testrole2");		
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("secured"), Arrays.asList(tag1, tag2));
				
		KnowledgeSecurityTag tag2_copy = factory.createKnowledgeSecurityTag();
		tag2_copy.setRequiredRole(factory.createSecurityRole());
		tag2_copy.getRequiredRole().setRoleName("testrole2");		
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("secured2"), Arrays.asList(tag2_copy));
		
		// when encryptValueSet() is called
		List<KnowledgeData> result = target.encryptValueSet(valueSet, localKnowledgeManager, metaData);
		
		// then data secured data are encrypted
		assertEquals(3, result.size()); // "b" and "c" plain, "secured" and "secured2" encrypted
		KnowledgeData plainData = result.get(0);
		KnowledgeData securedDataForRole1 = result.get(1);
		KnowledgeData securedDataForRole2 = result.get(2);
		
		assertEquals(2, plainData.getKnowledge().getKnowledgePaths().size());
		assertEquals("TEST", plainData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("b")));
		assertEquals(123, plainData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("c")));
		assertFalse(plainData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("b")) instanceof SealedObject);
		assertFalse(plainData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("c")) instanceof SealedObject);
		assertNull(plainData.getMetaData().encryptedKey);
		assertNull(plainData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured")));
		assertNull(plainData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured2")));
		assertNotNull(plainData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("b")));
		assertNotNull(plainData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("c")));
		assertEquals(2, plainData.getAuthors().getKnowledgePaths().size());
		
		assertNotNull(securedDataForRole1.getMetaData().encryptedKey);
		assertNotNull(securedDataForRole1.getMetaData().encryptedKeyAlgorithm);
		assertNotNull(securedDataForRole1.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured")));
		assertTrue(securedDataForRole1.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		assertNull(securedDataForRole1.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured2")));
		assertEquals(1, securedDataForRole1.getAuthors().getKnowledgePaths().size());
		
		assertNotNull(securedDataForRole2.getMetaData().encryptedKey);
		assertNotNull(securedDataForRole2.getMetaData().encryptedKeyAlgorithm);
		assertNotNull(securedDataForRole2.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured")));
		assertTrue(securedDataForRole2.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		assertTrue(securedDataForRole2.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured2")) instanceof SealedObject);
		assertEquals(2, securedDataForRole2.getAuthors().getKnowledgePaths().size());
		
		assertEquals(2, plainData.getSecuritySet().getKnowledgePaths().size());
		assertEquals(1, securedDataForRole1.getSecuritySet().getKnowledgePaths().size());
		assertEquals(2, securedDataForRole2.getSecuritySet().getKnowledgePaths().size());
		
		assertTrue(securedDataForRole1.getSecuritySet().getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		assertTrue(securedDataForRole2.getSecuritySet().getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		assertTrue(securedDataForRole2.getSecuritySet().getValue(RuntimeModelHelper.createKnowledgePath("secured2")) instanceof SealedObject);
	}
	
	@Test
	public void decryptValueSet_NullTest() {
		// given null is passed
				
		// when decryptValueSet() is called
		assertNull(target.decryptValueSet(null, localKnowledgeManager, metaData));
		
		// then no exception is thrown
	}
	
	@Test
	public void decryptValueSet_NoSecurityTestWithPlaintextSignDisabled() throws InvalidKeyException, CertificateEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, SignatureException, KeyStoreException, SecurityException, IllegalStateException, IOException {
		// given no security is used
		System.setProperty(DeecoProperties.SIGN_PLAINTEXT_MESSAGES, "false");
		
		// re-create the target to accepted the new system property value
		target = new KnowledgeEncryptor(keyManagerMock);
		
		// given no security is used		
		ValueSet knowledge = new ValueSet();
		knowledge.setValue(RuntimeModelHelper.createKnowledgePath("b"), "TEST");
		ValueSet authors = new ValueSet();
		authors.setValue(RuntimeModelHelper.createKnowledgePath("b"), "AUTHOR");
		KnowledgeData data = new KnowledgeData(knowledge, new ValueSet(), authors, metaData);
		
		metaData.signature = null;
		
		// when decryptValueSet() is called
		KnowledgeData decryptedData = target.decryptValueSet(data, localKnowledgeManager, metaData);
		
		// then data are not modified
		assertEquals("TEST", decryptedData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("b")));
		assertEquals("AUTHOR", decryptedData.getAuthors().getValue(RuntimeModelHelper.createKnowledgePath("b")));
		assertNotSame(decryptedData.getKnowledge(), knowledge);
		assertNotSame(decryptedData.getAuthors(), authors);
	}
	
	@Test
	public void decryptValueSet_NoSecurityTestWithPlaintextSignEnabled() throws InvalidKeyException, CertificateEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, SignatureException, KeyStoreException, SecurityException, IllegalStateException, IOException {
		// given no security is used
		System.setProperty(DeecoProperties.SIGN_PLAINTEXT_MESSAGES, "true");
		
		// re-create the target to accepted the new system property value
		target = new KnowledgeEncryptor(keyManagerMock);
		
		// given no security is used		
		ValueSet knowledge = new ValueSet();
		knowledge.setValue(RuntimeModelHelper.createKnowledgePath("b"), "TEST");
		ValueSet authors = new ValueSet();
		authors.setValue(RuntimeModelHelper.createKnowledgePath("b"), "AUTHOR");
		KnowledgeData data = new KnowledgeData(knowledge, new ValueSet(), authors, metaData);
		
		metaData.signature = securityHelper.sign(keyManagerMock.getIntegrityPrivateKey(), 
				metaData.componentId, metaData.versionId, metaData.targetRoleHash, 
				knowledge, new ValueSet(), authors);
		
		// when decryptValueSet() is called
		KnowledgeData decryptedData = target.decryptValueSet(data, localKnowledgeManager, metaData);
		
		// then data are not modified
		assertEquals("TEST", decryptedData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("b")));
		assertEquals("AUTHOR", decryptedData.getAuthors().getValue(RuntimeModelHelper.createKnowledgePath("b")));
		assertNotSame(decryptedData.getKnowledge(), knowledge);
		assertNotSame(decryptedData.getAuthors(), authors);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void decryptValueSet_SecurityTest() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, ShortBufferException, BadPaddingException, SignatureException, CertificateEncodingException, KeyStoreException, SecurityException, IllegalStateException {
		// given security is used	
		KnowledgeSecurityTag tag = factory.createKnowledgeSecurityTag();
		SecurityRole role = factory.createSecurityRole();
		role.setRoleName("testrole1");		
		tag.setRequiredRole(role);
		
		Key symmetricKey = securityHelper.generateKey();
		metaData.encryptedKey = securityHelper.encryptKey(symmetricKey, testrole1PublicKey);
		metaData.encryptedKeyAlgorithm = symmetricKey.getAlgorithm();
		metaData.targetRoleHash = keyManagerMock.getRoleKey("testrole1", Collections.emptyMap());		
		
		Cipher cipher = securityHelper.getSymmetricCipher(Cipher.ENCRYPT_MODE, symmetricKey);
		SealedObject sealedKnowledge = new SealedObject(666, cipher);
		
		ValueSet valueSet2 = new ValueSet();
		valueSet2.setValue(RuntimeModelHelper.createKnowledgePath("secured"), sealedKnowledge);
		
		ValueSet securitySet = new ValueSet();
		SealedObject sealedTags = new SealedObject( (Serializable)Arrays.asList(tag), cipher);
		securitySet.setValue(RuntimeModelHelper.createKnowledgePath("secured"), sealedTags);		
		
		ValueSet authorsSet = new ValueSet();
		SealedObject sealedAuthor = new SealedObject("author", cipher);
		authorsSet.setValue(RuntimeModelHelper.createKnowledgePath("secured"), sealedAuthor);	
		
		metaData.signature = securityHelper.sign(keyManagerMock.getIntegrityPrivateKey(), 
				metaData.componentId, metaData.versionId, metaData.targetRoleHash, 
				valueSet2, securitySet, authorsSet);
		KnowledgeData data = new KnowledgeData(valueSet2, securitySet, authorsSet, metaData);
		
		assertTrue(valueSet2.getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		assertTrue(securitySet.getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		assertTrue(authorsSet.getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		
		// when decryptValueSet() is called
		KnowledgeData decryptedData = target.decryptValueSet(data, localKnowledgeManager, metaData);
		
		// then data are decrypted
		assertEquals(666, decryptedData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured")));
		assertNotSame(valueSet2, decryptedData.getKnowledge());
		assertNotSame(securitySet, decryptedData.getSecuritySet());
		assertNotSame(authorsSet, decryptedData.getAuthors());
		
		List<KnowledgeSecurityTag> tags = (List<KnowledgeSecurityTag>) decryptedData.getSecuritySet().getValue(RuntimeModelHelper.createKnowledgePath("secured"));
		assertEquals(tag, tags.get(0));
		assertNotSame(tag, tags.get(0));
		
		assertEquals("author", decryptedData.getAuthors().getValue(RuntimeModelHelper.createKnowledgePath("secured")));
	}
	
	@Test
	public void decryptValueSet_WrongKeyTest() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, ShortBufferException, BadPaddingException, CertificateEncodingException, SignatureException, KeyStoreException, SecurityException, IllegalStateException {
		// given security is used
		Key symmetricKey = securityHelper.generateKey();
		metaData.encryptedKey = securityHelper.encryptKey(symmetricKey, testrole1PublicKey);
		metaData.encryptedKeyAlgorithm = symmetricKey.getAlgorithm();
		metaData.targetRoleHash = keyManagerMock.getRoleKey("testrole1", null);
		metaData.signature = securityHelper.sign(keyManagerMock.getIntegrityPrivateKey(), metaData.componentId, metaData.versionId, metaData.targetRoleHash);
				
		// when testrole2 key is used to encrypt data
		Cipher cipher = securityHelper.getSymmetricCipher(Cipher.ENCRYPT_MODE, testrole2PublicKey);
		SealedObject sealed = new SealedObject(666, cipher);
		
		ValueSet valueSet2 = new ValueSet();
		valueSet2.setValue(RuntimeModelHelper.createKnowledgePath("secured"), sealed);
		KnowledgeData data = new KnowledgeData(valueSet2, new ValueSet(), new ValueSet(), metaData);
		
		assertTrue(valueSet2.getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		
		// when decryptValueSet() is called
		KnowledgeData decryptedData = target.decryptValueSet(data, localKnowledgeManager, metaData);
		
		// then knowledge is removed
		assertNull(decryptedData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured")));
		assertEquals(0, decryptedData.getKnowledge().getKnowledgePaths().size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void encryptDecryptTest() throws KnowledgeNotFoundException {
		// given local component has testrole1
		KnowledgeSecurityTag tag1 = factory.createKnowledgeSecurityTag();
		tag1.setRequiredRole(factory.createSecurityRole());
		tag1.getRequiredRole().setRoleName("testrole1");
		
		WildcardSecurityTag tag2 = factory.createWildcardSecurityTag();
		tag2.setAccessRights(AccessRights.READ);
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("secured"), Arrays.asList(tag1, tag2));
		
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("secured2"), Arrays.asList(createAllowEveryoneTag()));
		
		// when data are encrypted
		List<KnowledgeData> kds = target.encryptValueSet(valueSet, localKnowledgeManager, metaData);
		KnowledgeData encryptedData = kds.get(1);
		ValueSet valueSet = encryptedData.getKnowledge();
		ValueSet securitySet = encryptedData.getSecuritySet();
		ValueSet authorSet = encryptedData.getAuthors();
		
		// then SealedObject is used
		assertTrue(valueSet.getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		assertTrue(securitySet.getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		assertTrue(authorSet.getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		
		// when data are decrypted
		KnowledgeData decryptedData = target.decryptValueSet(encryptedData, replicaKnowledgeManager, encryptedData.getMetaData());
		
		// then actual value is restored
		assertEquals(666, decryptedData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured")));
		
		List<WildcardSecurityTag> decryptedTags = (List<WildcardSecurityTag>) decryptedData.getSecuritySet().getValue(RuntimeModelHelper.createKnowledgePath("secured"));
		assertEquals(tag1.getRequiredRole().getRoleName(), ((KnowledgeSecurityTag) decryptedTags.get(0)).getRequiredRole().getRoleName());
		assertEquals(tag1.getRequiredRole().getArguments().size(), ((KnowledgeSecurityTag) decryptedTags.get(0)).getRequiredRole().getArguments().size());
		assertEquals(tag1.getRequiredRole().getConsistsOf().size(), ((KnowledgeSecurityTag) decryptedTags.get(0)).getRequiredRole().getConsistsOf().size());
		
		assertEquals(tag2.getAccessRights(), decryptedTags.get(1).getAccessRights());
		
		assertEquals("author_secured", decryptedData.getAuthors().getValue(RuntimeModelHelper.createKnowledgePath("secured")));
	}
	
	
}
