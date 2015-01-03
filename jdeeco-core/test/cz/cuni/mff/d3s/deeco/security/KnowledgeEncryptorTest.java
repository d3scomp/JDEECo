package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.ShortBufferException;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeMetaData;

public class KnowledgeEncryptorTest {

	private KnowledgeEncryptor target;
	private SecurityKeyManager keyManager;
	private BaseKnowledgeManager localKnowledgeManager, replicaKnowledgeManager;
	private ValueSet valueSet;
	private KnowledgeMetaData metaData;
	private RuntimeMetadataFactory factory;
	private SecurityHelper securityHelper;
	
	private Key testrole1PublicKey, testrole2PublicKey;
	
	@Before
	public void setUp() throws InvalidKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, SecurityException, SignatureException, IllegalStateException {
		factory = RuntimeMetadataFactoryExt.eINSTANCE;
		
		valueSet = new ValueSet();
		valueSet.setValue(RuntimeModelHelper.createKnowledgePath("b"), "TEST");
		valueSet.setValue(RuntimeModelHelper.createKnowledgePath("c"), 123);
		valueSet.setValue(RuntimeModelHelper.createKnowledgePath("secured"), 666);
		
		metaData = new KnowledgeMetaData("xy", 13, "A", 123456, 45);
		ComponentInstance component = mock(ComponentInstance.class);
		
		localKnowledgeManager = new BaseKnowledgeManager("sender_id", component);
		
		when(component.getKnowledgeManager()).thenReturn(localKnowledgeManager);
	 	SecurityRole role = factory.createSecurityRole();
	 	role.setRoleName("testrole1");
	 	ComponentInstance tmpComponent = factory.createComponentInstance();
	 	tmpComponent.getRoles().add(role);
		when(component.getRoles()).thenReturn(tmpComponent.getRoles());
		
		replicaKnowledgeManager = new BaseKnowledgeManager("receiver_id", component);
		securityHelper = new SecurityHelper();
		
		keyManager = mock(SecurityKeyManager.class);
		testrole1PublicKey = securityHelper.generateKey();	
		testrole2PublicKey = securityHelper.generateKey();	
		
		when(keyManager.getPublicKeyFor(eq("testrole1"), anyObject())).thenReturn(testrole1PublicKey);
		when(keyManager.getPrivateKeyFor(eq("testrole1"), anyObject())).thenReturn(testrole1PublicKey);
		when(keyManager.getPublicKeyFor(eq("testrole2"), anyObject())).thenReturn(testrole2PublicKey);
		
		target = new KnowledgeEncryptor(keyManager);
	}
	
	@Test
	public void encryptValueSet_NullTest() throws KnowledgeNotFoundException {
		// given null is passed
		
		// when encryptValueSet() is called
		List<KnowledgeData> result = target.encryptValueSet(null, localKnowledgeManager, metaData);
		
		assertNull(result);
	}
	
	@Test
	public void encryptValueSet_NoSecurityTest() throws KnowledgeNotFoundException {
		// given no security is used
		
		// when encryptValueSet() is called
		List<KnowledgeData> result = target.encryptValueSet(valueSet, localKnowledgeManager, metaData);
		
		// then data are not encrypted and not modified
		assertEquals(1, result.size());
		assertEquals(metaData, result.get(0).getMetaData());
		assertEquals(valueSet, result.get(0).getKnowledge());
		
		for (KnowledgePath kp : result.get(0).getKnowledge().getKnowledgePaths()) {
			assertFalse(result.get(0).getKnowledge().getValue(kp) instanceof SealedObject);
		}
		
		assertNull(result.get(0).getMetaData().encryptedKey);
	}
	
	@Test
	public void encryptValueSet_SecurityTest() throws KnowledgeNotFoundException {
		// given single security tag is used
		Collection<KnowledgeSecurityTag> tags = new LinkedList<>();
		KnowledgeSecurityTag tag1 = factory.createKnowledgeSecurityTag();
		tag1.setRoleName("testrole1");
		KnowledgeSecurityTag tag2 = factory.createKnowledgeSecurityTag();
		tag2.setRoleName("testrole2");
		tags.add(tag1);
		tags.add(tag2);
		localKnowledgeManager.markAsSecured(RuntimeModelHelper.createKnowledgePath("secured"), tags);
		
		// when encryptValueSet() is called
		List<KnowledgeData> result = target.encryptValueSet(valueSet, localKnowledgeManager, metaData);
		
		// then data secured data are encrypted
		assertEquals(3, result.size()); // "b" and "c" plain, "secured" encrypted
		KnowledgeData plainData = result.get(0);
		KnowledgeData securedDataForRole1 = result.get(1);
		KnowledgeData securedDataForRole2 = result.get(2);
		
		assertEquals(2, plainData.getKnowledge().getKnowledgePaths().size());
		assertEquals("TEST", plainData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("b")));
		assertEquals(123, plainData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("c")));
		assertFalse(plainData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("b")) instanceof SealedObject);
		assertFalse(plainData.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("c")) instanceof SealedObject);
		assertNull(plainData.getMetaData().encryptedKey);
		
		assertNotNull(securedDataForRole1.getMetaData().encryptedKey);
		assertNotNull(securedDataForRole1.getMetaData().encryptedKeyAlgorithm);
		assertNotNull(securedDataForRole1.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured")));
		assertTrue(securedDataForRole1.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		
		assertNotNull(securedDataForRole2.getMetaData().encryptedKey);
		assertNotNull(securedDataForRole2.getMetaData().encryptedKeyAlgorithm);
		assertNotNull(securedDataForRole2.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured")));
		assertTrue(securedDataForRole2.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
	}
	
	@Test
	public void decryptChangeSet_NullTest() {
		// given null is passed
				
		// when decryptChangeSet() is called
		target.decryptChangeSet(null, localKnowledgeManager, metaData);
		
		// then no exception is thrown
	}
	
	@Test
	public void decryptChangeSet_NoSecurityTest() {
		// given no security is used
		ChangeSet changeSet1 = new ChangeSet();
		changeSet1.setValue(RuntimeModelHelper.createKnowledgePath("b"), "TEST");
		
		// when decryptChangeSet() is called
		target.decryptChangeSet(changeSet1, localKnowledgeManager, metaData);
		
		// then data are not modified
		assertEquals("TEST", changeSet1.getValue(RuntimeModelHelper.createKnowledgePath("b")));
	}
	
	@Test
	public void decryptChangeSet_SecurityTest() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, ShortBufferException, BadPaddingException {
		// given security is used
		metaData.encryptedKey = securityHelper.encryptKey(testrole1PublicKey, testrole1PublicKey);
		metaData.encryptedKeyAlgorithm = testrole1PublicKey.getAlgorithm();
		
		Cipher cipher = securityHelper.getSymmetricCipher(Cipher.ENCRYPT_MODE, testrole1PublicKey);
		SealedObject sealed = new SealedObject(666, cipher);
		
		ChangeSet changeSet2 = new ChangeSet();
		changeSet2.setValue(RuntimeModelHelper.createKnowledgePath("secured"), sealed);
		
		assertTrue(changeSet2.getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		
		// when decryptChangeSet() is called
		target.decryptChangeSet(changeSet2, localKnowledgeManager, metaData);
		
		// then data are decrypted
		assertEquals(666, changeSet2.getValue(RuntimeModelHelper.createKnowledgePath("secured")));
	}
	
	@Test
	public void decryptChangeSet_WrongKeyTest() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, ShortBufferException, BadPaddingException {
		// given security is used
		metaData.encryptedKey = securityHelper.encryptKey(testrole1PublicKey, testrole1PublicKey);
		metaData.encryptedKeyAlgorithm = testrole1PublicKey.getAlgorithm();
		
		// when testrole2 key is used to encrypt data
		Cipher cipher = securityHelper.getSymmetricCipher(Cipher.ENCRYPT_MODE, testrole2PublicKey);
		SealedObject sealed = new SealedObject(666, cipher);
		
		ChangeSet changeSet2 = new ChangeSet();
		changeSet2.setValue(RuntimeModelHelper.createKnowledgePath("secured"), sealed);
		
		assertTrue(changeSet2.getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		
		// when decryptChangeSet() is called
		target.decryptChangeSet(changeSet2, localKnowledgeManager, metaData);
		
		// then knowledge is removed
		assertNull(changeSet2.getValue(RuntimeModelHelper.createKnowledgePath("secured")));
		assertEquals(0, changeSet2.getUpdatedReferences().size());
	}
	
	@Test
	public void encryptDecryptTest() throws KnowledgeNotFoundException {
		// given local component has testrole1
		Collection<KnowledgeSecurityTag> tags = new LinkedList<>();
		KnowledgeSecurityTag tag1 = factory.createKnowledgeSecurityTag();
		tag1.setRoleName("testrole1");
		tags.add(tag1);
		localKnowledgeManager.markAsSecured(RuntimeModelHelper.createKnowledgePath("secured"), tags);
		
		// when data are encrypted
		List<KnowledgeData> kds = target.encryptValueSet(valueSet, localKnowledgeManager, metaData);
		KnowledgeData encryptedData = kds.get(1);
		ChangeSet changeSet = toChangeSet(encryptedData.getKnowledge());
		
		// then SealedObject is used
		assertTrue(changeSet.getValue(RuntimeModelHelper.createKnowledgePath("secured")) instanceof SealedObject);
		
		// when data are decrypted
		target.decryptChangeSet(changeSet, replicaKnowledgeManager, encryptedData.getMetaData());
		
		// then actual value is restored
		assertEquals(666, changeSet.getValue(RuntimeModelHelper.createKnowledgePath("secured")));
	}
	
	protected ChangeSet toChangeSet(ValueSet valueSet) {
		if (valueSet != null) {
			ChangeSet result = new ChangeSet();
			for (KnowledgePath kp : valueSet.getKnowledgePaths())
				result.setValue(kp, valueSet.getValue(kp));
			return result;
		} else {
			return null;
		}
	}
}
