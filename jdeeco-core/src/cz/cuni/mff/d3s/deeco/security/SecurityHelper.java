package cz.cuni.mff.d3s.deeco.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Helper class containing low-level functionality for working with security keys.
 * @author Ondřej Štumpf  
 */
public class SecurityHelper {

	private final int BLOCK_SIZE = 16;
	private static SecureRandom secureRandom = new SecureRandom();
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	/**
	 * Encrypts the symmetric key using the public key.
	 *
	 * @param symmetricKey
	 *            the symmetric key
	 * @param publicKey
	 *            the public key
	 * @return the encrypted symmetric key
	 */
	public byte[] encryptKey(Key symmetricKey, Key publicKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, IOException {
		Cipher publicCipher = getAsymmetricCipher(Cipher.ENCRYPT_MODE, publicKey);
		byte[] input = symmetricKey.getEncoded();
				
		return transform(input, publicCipher);		
	}

	/**
	 * Decrypts the symmetric key with specified algorithm using given private key.
	 *
	 * @param encryptedKey
	 *            the encrypted key
	 * @param algorithm
	 *            the algorithm
	 * @param privateKey
	 *            the private key
	 * @return the decrypted key
	 */
	public Key decryptKey(byte[] encryptedKey, String algorithm, Key privateKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, IOException {
		Cipher publicCipher = getAsymmetricCipher(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedKey = transform(encryptedKey, publicCipher);
		
		return new SecretKeySpec(decryptedKey, algorithm);		
	}
	
	/**
	 * Transforms (encrypts/decrypts) the array of bytes using given cipher.
	 *
	 * @param data
	 *            the data
	 * @param cipher
	 *            the cipher
	 * @return the transformed data
	 */
	public byte[] transform(byte[] data, Cipher cipher) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException, IOException {
		byte[] buffer = new byte[BLOCK_SIZE];
		int noBytes = 0;
        byte[] cipherBlock = new byte[cipher.getOutputSize(buffer.length)];
        int cipherBytes;
       
        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        
        try {
	        inputStream = new ByteArrayInputStream(data);	        
	        outputStream = new ByteArrayOutputStream();
	        
	        while((noBytes = inputStream.read(buffer)) != -1) {
	           cipherBytes = cipher.update(buffer, 0, noBytes, cipherBlock);
	           outputStream.write(cipherBlock, 0, cipherBytes);       
	        }
	       
	        cipherBytes = cipher.doFinal(cipherBlock, 0);
	        outputStream.write(cipherBlock, 0, cipherBytes);
        } finally {
        	if (inputStream != null) inputStream.close();
        	if (outputStream != null) outputStream.close();
        }
        
        return outputStream.toByteArray();
	}
	
	/**
	 * Gets the symmetric cipher for the given symmetric key.
	 *
	 * @param opmode
	 *            the operation mode {@link Cipher}
	 * @param symmetricKey
	 *            the symmetric key
	 * @return the symmetric cipher
	 */
	public Cipher getSymmetricCipher(int opmode, Key symmetricKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance(symmetricKey.getAlgorithm());
		cipher.init(opmode, symmetricKey);		
		return cipher;
	}

	/**
	 * Gets the asymmetric cipher for the given key.
	 *
	 * @param opmode
	 *            the operation mode {@link Cipher}
	 * @param assymetricKey
	 *            the public/private key
	 * @return the asymmetric cipher
	 */
	public Cipher getAsymmetricCipher(int opmode, Key assymetricKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance(assymetricKey.getAlgorithm());
		cipher.init(opmode, assymetricKey);		
		return cipher;
	}
	
	/**
	 * Generates random AES key of size 128b. WARNING: raising the key size requries the JCE to be installed!!.
	 *
	 * @return AES key of size 128b
	 */
	public Key generateKey() throws NoSuchAlgorithmException {
		return generateKey("AES", 128);
	}

	/**
	 * Generates new symmetric key.
	 *
	 * @param algorithm
	 *            the algorithm of the key
	 * @param keySize
	 *            the key size
	 * @return the key
	 */
	public Key generateKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
		KeyGenerator generator = KeyGenerator.getInstance(algorithm);
		generator.init(keySize); 
		generator.init(secureRandom);
		return generator.generateKey();
	}

	/**
	 * Generates new key pair with the RSA algorithm and 1024b key size.
	 *
	 * @return the key pair
	 */
	public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		return generateKeyPair("RSA", 1024);
	}
	
	/**
	 * Generates new key pair.
	 *
	 * @param algorithm
	 *            the algorithm
	 * @param keySize
	 *            the key size
	 * @return the key pair
	 */
	public KeyPair generateKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
		generator.initialize(keySize, secureRandom);
		return generator.generateKeyPair();
	}
	
	/**
	 * Sign the given data using the private key.
	 *
	 * @param key
	 *            the key
	 * @param data
	 *            the data
	 * @return the signature
	 */
	public byte[] sign(PrivateKey key, Object... data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, SignatureException {
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException("No data to sign.");
		}
		
		int hash = Arrays.hashCode(data);
		byte[] hashArray = new byte[] { (byte)(hash >>> 24), (byte)(hash >>> 16), (byte)(hash >>> 8), (byte)hash};

		Signature signature = Signature.getInstance(key.getAlgorithm());
		signature.initSign(key);
		signature.update(hashArray);
		return signature.sign();
	}

	/**
	 * Verify the signature.
	 *
	 * @param signatureHash
	 *            the signature hash
	 * @param key
	 *            the key
	 * @param data
	 *            the data
	 * @return true, if successful
	 */
	public boolean verify(byte[] signatureHash, PublicKey key, Object... data) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException("No data to verify.");
		}
		
		int hash = Arrays.hashCode(data);
		byte[] hashArray = new byte[] { (byte)(hash >>> 24), (byte)(hash >>> 16), (byte)(hash >>> 8), (byte)hash};

		Signature signature = Signature.getInstance(key.getAlgorithm());
		signature.initVerify(key);
		signature.update(hashArray);
		return signature.verify(signatureHash);
	}

}
