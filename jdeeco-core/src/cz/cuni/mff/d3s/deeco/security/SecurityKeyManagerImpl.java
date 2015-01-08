package cz.cuni.mff.d3s.deeco.security;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;


/**
 * @author Ondřej Štumpf  
 */
public class SecurityKeyManagerImpl implements SecurityKeyManager {

	private KeyStore keyStore;
	private Map<String, PrivateKey> privateKeys;
	private SecureRandom secureRandom;
	
	public SecurityKeyManagerImpl() throws KeyStoreException {
		this.keyStore = KeyStore.getInstance("JKS");
		this.secureRandom = new SecureRandom();
		this.privateKeys = new HashMap<>();
		
		try {
			initialize();
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new KeyStoreException(e);
		}
		
	}
	
	private void initialize() throws NoSuchAlgorithmException, CertificateException, IOException {
		keyStore.load(null, null);
	}

	@Override
	public Key getPublicKeyFor(String roleName, List<Object> arguments) throws KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, CertificateEncodingException, SecurityException, SignatureException, IllegalStateException {
		String roleKey = getRoleKey(roleName, arguments);
		
		if (!keyStore.containsAlias(roleKey)) {
			generateKeyPairFor(roleKey);
		}
		
		return keyStore.getCertificate(roleKey).getPublicKey();
	}

	private void generateKeyPairFor(String roleKey) throws NoSuchAlgorithmException, InvalidKeyException, CertificateEncodingException, KeyStoreException, SecurityException, SignatureException, IllegalStateException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024, secureRandom);
        KeyPair keypair = keyGen.generateKeyPair();
		
		keyStore.setCertificateEntry(roleKey, generateCertificate(roleKey, keypair));
		privateKeys.put(roleKey, keypair.getPrivate());
	}

	@Override
	public Key getPrivateKeyFor(String roleName, List<Object> arguments) throws InvalidKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, SecurityException, SignatureException, IllegalStateException {
		String roleKey = getRoleKey(roleName, arguments);
		
		if (!keyStore.containsAlias(roleKey)) {
			generateKeyPairFor(roleKey);
		}
		
		return privateKeys.get(roleKey);
	}
	
	@SuppressWarnings("deprecation")
	private Certificate generateCertificate(String roleKey, KeyPair keypair) throws NoSuchAlgorithmException, InvalidKeyException, SecurityException, SignatureException, CertificateEncodingException, IllegalStateException {		
        X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();
        v3CertGen.setSerialNumber(BigInteger.valueOf(Math.abs(secureRandom.nextInt())));
        v3CertGen.setIssuerDN(new X509Principal("CN=local, OU=None, O=None L=None, C=None"));
        v3CertGen.setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30));
        v3CertGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365*10)));
        v3CertGen.setSubjectDN(new X509Principal("CN=" + roleKey + ", OU=None, O=None L=None, C=None"));
        
        v3CertGen.setPublicKey(keypair.getPublic());
        v3CertGen.setSignatureAlgorithm("MD5WithRSA"); 
        
        return v3CertGen.generate(keypair.getPrivate());
	}

	public String getRoleKey(String roleName, List<Object> arguments) {
		if (arguments == null) {
			arguments = Collections.emptyList();
		}
		int hash = roleName.hashCode()+arguments.stream().mapToInt(a -> a.hashCode()).sum();
		return "role" + hash;
	}

}
