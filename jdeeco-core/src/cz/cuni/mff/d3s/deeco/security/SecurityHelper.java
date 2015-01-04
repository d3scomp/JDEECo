package cz.cuni.mff.d3s.deeco.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @author Ondřej Štumpf  
 */
public class SecurityHelper {

	private final int BLOCK_SIZE = 16;
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public byte[] encryptKey(Key symmetricKey, Key publicKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, IOException {
		Cipher publicCipher = getAsymmetricCipher(Cipher.ENCRYPT_MODE, publicKey);
		byte[] input = symmetricKey.getEncoded();
				
		return transform(input, publicCipher);		
	}

	public Key decryptKey(byte[] encryptedKey, String algorithm, Key privateKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, IOException {
		Cipher publicCipher = getAsymmetricCipher(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedKey = transform(encryptedKey, publicCipher);
		
		return new SecretKeySpec(decryptedKey, algorithm);		
	}
	
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
	
	public Cipher getSymmetricCipher(int opmode, Key symmetricKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance(symmetricKey.getAlgorithm());
		cipher.init(opmode, symmetricKey);		
		return cipher;
	}

	public Cipher getAsymmetricCipher(int opmode, Key publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(opmode, publicKey);		
		return cipher;
	}
	
	public Key generateKey() throws NoSuchAlgorithmException {
		return generateKey("AES", 256);
	}

	public Key generateKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
		KeyGenerator generator = KeyGenerator.getInstance(algorithm);
		generator.init(keySize); 
		return generator.generateKey();
	}

}
