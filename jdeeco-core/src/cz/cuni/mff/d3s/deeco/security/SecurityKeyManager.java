package cz.cuni.mff.d3s.deeco.security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.util.List;

/**
 * @author Ondřej Štumpf  
 */
public interface SecurityKeyManager {

	Key getPublicKeyFor(String roleName, List<Object> arguments) throws KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, CertificateEncodingException, SecurityException, SignatureException, IllegalStateException;

	Key getPrivateKeyFor(String roleName, List<Object> arguments) throws InvalidKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, SecurityException, SignatureException, IllegalStateException;

	String getRoleKey(String roleName, List<Object> arguments);
}
