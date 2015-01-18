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
 * @author Ondřej Štumpf  
 */
public interface SecurityKeyManager {

	PublicKey getPublicKeyFor(String roleName, Map<String, Object> arguments) throws KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, CertificateEncodingException, SecurityException, SignatureException, IllegalStateException;

	PrivateKey getPrivateKeyFor(String roleName, Map<String, Object> arguments) throws InvalidKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, SecurityException, SignatureException, IllegalStateException;
	
	PrivateKey getIntegrityPrivateKey() throws InvalidKeyException, CertificateEncodingException, NoSuchAlgorithmException, KeyStoreException, SecurityException, SignatureException, IllegalStateException;

	PublicKey getIntegrityPublicKey() throws InvalidKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, SecurityException, SignatureException, IllegalStateException;
}
