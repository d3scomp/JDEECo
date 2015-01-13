package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Ondřej Štumpf  
 */
public class SecurityHelperTest {

	private SecurityHelper target;
	
	@Before
	public void setUp() {
		target = new SecurityHelper();
	}
	
	@Test
	public void generateSymmetricKeyTest() throws NoSuchAlgorithmException {
		// when generateKey() is called
		Key key = target.generateKey();
		
		// then default key is generated
		assertEquals("AES", key.getAlgorithm());
	}
	
	@Test
	public void getSymmetricCipherTest1() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		// given default key is generated
		Key key = target.generateKey();
		
		// when getSymmetricCipher() is called
		Cipher cipher = target.getSymmetricCipher(Cipher.DECRYPT_MODE, key);
		
		// then Cipher is properly generated
		assertEquals("AES", cipher.getAlgorithm());
	}
	
	@Test
	public void getAsymmetricCipherTest1() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
		// given public key is prepared
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		Key key = generator.generateKeyPair().getPublic();
		
		// when getAsymmetricCipher() is called
		Cipher cipher = target.getAsymmetricCipher(Cipher.DECRYPT_MODE, key);
		
		// then Cipher is properly generated
		assertEquals("RSA", cipher.getAlgorithm());
	}
	
	@Test
	public void getAsymmetricCipherTest2() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
		// given private key is prepared
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		Key key = generator.generateKeyPair().getPrivate();
		
		// when getAsymmetricCipher() is called
		Cipher cipher = target.getAsymmetricCipher(Cipher.DECRYPT_MODE, key);
		
		// then Cipher is properly generated
		assertEquals("RSA", cipher.getAlgorithm());
	}
	
	@Test
	public void keyEncryptionTest() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ShortBufferException, IOException {
		// given keys are prepared
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		KeyPair keyPair = generator.generateKeyPair();
		
		Key symmetricKey = target.generateKey("AES", 512);
		Key publicKey = keyPair.getPublic();
		
		// when encryptKey() is called
		byte[] bytes = target.encryptKey(symmetricKey, publicKey);
		
		// when decryptKey() is called()
		Key decryptedKey = target.decryptKey(bytes, "AES", keyPair.getPrivate());
		
		// then keys are equal
		assertEquals(symmetricKey, decryptedKey);
	}
	
	@Test
	public void transformWithAsymmetricCipherTest() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, IOException {
		// given text 
		String text = "Lorem ipsum dolor sit amet."; // must be shorter then 117 bytes
		byte[] textBytes = text.getBytes();
		
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		KeyPair keyPair = generator.generateKeyPair();
		
		// when getAsymmetricCipher() is called
		Cipher encryptCipher = target.getAsymmetricCipher(Cipher.ENCRYPT_MODE, keyPair.getPublic());
		Cipher decryptCipher = target.getAsymmetricCipher(Cipher.DECRYPT_MODE, keyPair.getPrivate());
		
		// when transform is called
		byte[] encryptedBytes = target.transform(textBytes, encryptCipher);
		byte[] decryptedBytes = target.transform(encryptedBytes, decryptCipher);
		
		// then text is the same
		String decryptedText = new String(decryptedBytes);
		assertEquals(text, decryptedText);
	}
	
	@Test
	public void transformWithSymmetricCipherTest() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, IOException {
		// given text and key
		String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
		byte[] textBytes = text.getBytes();	
		Key key = target.generateKey();
		
		// when getSymmetricCipher() is called
		Cipher encryptCipher = target.getSymmetricCipher(Cipher.ENCRYPT_MODE, key);
		Cipher decryptCipher = target.getSymmetricCipher(Cipher.DECRYPT_MODE, key);
		
		// when transform is called
		byte[] encryptedBytes = target.transform(textBytes, encryptCipher);
		byte[] decryptedBytes = target.transform(encryptedBytes, decryptCipher);
		
		// then text is the same
		String decryptedText = new String(decryptedBytes);
		assertEquals(text, decryptedText);
	}
}
