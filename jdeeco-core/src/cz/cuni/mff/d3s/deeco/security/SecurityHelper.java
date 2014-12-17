package cz.cuni.mff.d3s.deeco.security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SecurityHelper {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public byte[] encryptKey(Key symmetricKey, Key publicKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher publicCipher = getAsymmetricCipher(Cipher.ENCRYPT_MODE, publicKey);
		return publicCipher.doFinal(symmetricKey.getEncoded());		
	}

	public Key decryptKey(byte[] encryptedKey, Key privateKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Cipher publicCipher = getAsymmetricCipher(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedKey = publicCipher.doFinal(encryptedKey);
		
		return new SecretKeySpec(decryptedKey, "AES");		
	}
	
	public Cipher getSymmetricCipher(int opmode, Key symmetricKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(opmode, symmetricKey);		
		return cipher;
	}

	public Cipher getAsymmetricCipher(int opmode, Key publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(opmode, publicKey);		
		return cipher;
	}
	
	public Key generateSymmetricKey() throws NoSuchAlgorithmException {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(256); //keysize
		return generator.generateKey();
	}

	

}
