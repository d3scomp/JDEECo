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
 * 
 * @author Ondřej Štumpf
 *
 */
public class RatingsEncryptor {
	private final SecurityKeyManager keyManager;
	private final SecurityHelper securityHelper;
	
	public RatingsEncryptor(SecurityKeyManager keyManager) {
		this.keyManager = keyManager;
		this.securityHelper = new SecurityHelper();
	}
	
	public List<SealedObject> encryptRatings(List<RatingsChangeSet> ratings, RatingsMetaData ratingsMetaData) {
		List<SealedObject> result = new ArrayList<>();
		
		try {
			Key publicKey = keyManager.getIntegrityPublicKey();
			Key symmetricKey = securityHelper.generateKey();
			byte[] encryptedKey = securityHelper.encryptKey(symmetricKey, publicKey);
			Cipher symmetricCipher = securityHelper.getSymmetricCipher(Cipher.ENCRYPT_MODE, symmetricKey);
			
			ratingsMetaData.encryptedKey = encryptedKey;
			ratingsMetaData.encryptedKeyAlgorithm = symmetricKey.getAlgorithm();
			
			for (RatingsChangeSet changeSet : ratings) {
				SealedObject encryptedChangeSet = new SealedObject((Serializable) changeSet, symmetricCipher);
				
				result.add(encryptedChangeSet);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return result;
	}
	
	public List<RatingsChangeSet> decryptRatings(List<SealedObject> ratings, RatingsMetaData ratingsMetaData) {
		List<RatingsChangeSet> result = new ArrayList<>();
		
		Key privateKey = null;
		Key decryptedSymmetricKey = null;
		
		try {
			privateKey = keyManager.getIntegrityPrivateKey();
			decryptedSymmetricKey = securityHelper.decryptKey(ratingsMetaData.encryptedKey, ratingsMetaData.encryptedKeyAlgorithm, privateKey);
		} catch (InvalidKeyException | CertificateEncodingException
				| NoSuchAlgorithmException | KeyStoreException
				| SecurityException | SignatureException
				| IllegalStateException | NoSuchPaddingException | ShortBufferException | IllegalBlockSizeException | BadPaddingException | IOException e1) {			
			e1.printStackTrace();
		}
		
		if (decryptedSymmetricKey != null) {
			for (SealedObject sealedObject : ratings) {
				try {
					RatingsChangeSet ratingsChangeSet = (RatingsChangeSet) sealedObject.getObject(securityHelper.getSymmetricCipher(Cipher.DECRYPT_MODE, decryptedSymmetricKey));
					result.add(ratingsChangeSet);
				} catch (InvalidKeyException | ClassNotFoundException
						| IllegalBlockSizeException | BadPaddingException
						| NoSuchAlgorithmException | NoSuchPaddingException
						| IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
}
