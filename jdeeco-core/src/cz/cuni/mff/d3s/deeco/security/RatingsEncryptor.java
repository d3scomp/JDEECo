package cz.cuni.mff.d3s.deeco.security;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.ShortBufferException;

import cz.cuni.mff.d3s.deeco.integrity.RatingsChangeSet;
import cz.cuni.mff.d3s.deeco.network.RatingsMetaData;

/**
 * Class used to encrypt ratings data (IDs of the components, the knowledge paths and ratings).
 * @author Ondřej Štumpf
 *
 */
public class RatingsEncryptor {
	private final SecurityKeyManager keyManager;
	private final SecurityHelper securityHelper;
	
	/**
	 * Instantiates a new ratings encryptor.
	 *
	 * @param keyManager
	 *            the key manager
	 */
	public RatingsEncryptor(SecurityKeyManager keyManager) {
		this.keyManager = keyManager;
		this.securityHelper = new SecurityHelper();
	}
	
	/**
	 * Encrypts ratings data and modifies given metadata accordingly.
	 *
	 * @param ratings
	 *            the ratings
	 * @param ratingsMetaData
	 *            the meta data
	 * @return the list of sealed objects
	 */
	public List<SealedObject> encryptRatings(List<RatingsChangeSet> ratings, RatingsMetaData ratingsMetaData) {
		List<SealedObject> result = new ArrayList<>();
		
		try {
			// get special well-known public key for this purpose
			Key publicKey = keyManager.getIntegrityPublicKey();
			
			// generate random symmetrical key
			Key symmetricKey = securityHelper.generateKey();
			
			// encrypt the symmetrical key with the public key and send it with data
			byte[] encryptedKey = securityHelper.encryptKey(symmetricKey, publicKey);
			Cipher symmetricCipher = securityHelper.getSymmetricCipher(Cipher.ENCRYPT_MODE, symmetricKey);
			
			ratingsMetaData.encryptedKey = encryptedKey;
			ratingsMetaData.encryptedKeyAlgorithm = symmetricKey.getAlgorithm();
			
			// use the cipher to encrypt everything in the list
			for (RatingsChangeSet changeSet : ratings) {
				SealedObject encryptedChangeSet = new SealedObject((Serializable) changeSet, symmetricCipher);
				
				result.add(encryptedChangeSet);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Decrypts ratings data.
	 *
	 * @param ratings
	 *            the encrypted ratings
	 * @param ratingsMetaData
	 *            the meta data
	 * @return the decrypted list
	 */
	public List<RatingsChangeSet> decryptRatings(List<SealedObject> ratings, RatingsMetaData ratingsMetaData) {
		List<RatingsChangeSet> result = new ArrayList<>();
		
		Key privateKey = null;
		Key decryptedSymmetricKey = null;
		
		try {
			// use well-known private key for decryption of the key from metadata
			privateKey = keyManager.getIntegrityPrivateKey();
			decryptedSymmetricKey = securityHelper.decryptKey(ratingsMetaData.encryptedKey, ratingsMetaData.encryptedKeyAlgorithm, privateKey);
		} catch (InvalidKeyException | CertificateEncodingException
				| NoSuchAlgorithmException | KeyStoreException
				| SecurityException | SignatureException
				| IllegalStateException | NoSuchPaddingException | ShortBufferException | IllegalBlockSizeException | BadPaddingException | IOException e1) {			
			// do nothing
		}
		
		if (decryptedSymmetricKey != null) {
			// decrypt everything in the list
			for (SealedObject sealedObject : ratings) {
				try {
					RatingsChangeSet ratingsChangeSet = (RatingsChangeSet) sealedObject.getObject(securityHelper.getSymmetricCipher(Cipher.DECRYPT_MODE, decryptedSymmetricKey));
					result.add(ratingsChangeSet);
				} catch (InvalidKeyException | ClassNotFoundException
						| IllegalBlockSizeException | BadPaddingException
						| NoSuchAlgorithmException | NoSuchPaddingException
						| IOException e) {
					
				}
			}
		}
		
		return result;
	}
}
