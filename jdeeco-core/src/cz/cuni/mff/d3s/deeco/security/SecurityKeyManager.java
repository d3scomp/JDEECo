package cz.cuni.mff.d3s.deeco.security;

import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.util.Map;


/**
 * The manager of certificates and public/private key pairs.
 *
 * @author Ondřej Štumpf
 */
public interface SecurityKeyManager {

	/**
	 * Gets the public key for the given role and concrete argument values.
	 *
	 * @param roleName
	 *            the role name
	 * @param arguments
	 *            the arguments
	 * @return the public key
	 */
	PublicKey getPublicKey(String roleName, Map<String, Object> arguments) throws KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, CertificateEncodingException, SecurityException, SignatureException, IllegalStateException;

	/**
	 * Gets the private key for the given role and concrete argument values.
	 *
	 * @param roleName
	 *            the role name
	 * @param arguments
	 *            the arguments
	 * @return the private key
	 */
	PrivateKey getPrivateKey(String roleName, Map<String, Object> arguments) throws InvalidKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, SecurityException, SignatureException, IllegalStateException;
	
	/**
	 * Gets the private key used for signatures and ratings decryption.
	 *
	 * @return the private key
	 */
	PrivateKey getIntegrityPrivateKey() throws InvalidKeyException, CertificateEncodingException, NoSuchAlgorithmException, KeyStoreException, SecurityException, SignatureException, IllegalStateException;

	/**
	 * Gets the public key used for signatures and ratings decryption.
	 *
	 * @return the public key
	 */
	PublicKey getIntegrityPublicKey() throws InvalidKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, SecurityException, SignatureException, IllegalStateException;
	
	/**
	 * Gets the unique key derived from the role name and its arguments. It also saves the 
	 * mapping from the key to the actual values.
	 * @param roleName
	 * 			the name of the role
	 * @param arguments
	 * 			the role arguments
	 * @return
	 * 			integer hash
	 */
	Integer getRoleKey(final String roleName, final Map<String, Object> arguments);
	
	/**
	 * Returns role and its arguments that was previously mapped using the getRoleKey() method.
	 * @param roleKey
	 * 			the integer hash
	 * @return
	 * 			role name and argument values
	 */
	RoleWithArguments getRoleByKey(Integer roleKey);
	
	/**
	 * Gets the public key of the CA.
	 *
	 * @return the public key
	 */
	PublicKey getAuthorityPublicKey() throws InvalidKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, SecurityException, SignatureException, IllegalStateException;

}
