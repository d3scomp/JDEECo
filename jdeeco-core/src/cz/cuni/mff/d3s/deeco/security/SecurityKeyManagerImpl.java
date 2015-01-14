package cz.cuni.mff.d3s.deeco.security;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;


/**
 * @author Ondřej Štumpf  
 */
public class SecurityKeyManagerImpl implements SecurityKeyManager {

	private KeyStore keyStore;
	private Map<Integer, PrivateKey> privateKeys;
	private SecureRandom secureRandom;
	private SecurityHelper securityHelper;
	
	private final Integer INTEGRITY_KEY = 95423814;
	
	public SecurityKeyManagerImpl() throws KeyStoreException {
		this.keyStore = KeyStore.getInstance("JKS");
		this.secureRandom = new SecureRandom();
		this.privateKeys = new HashMap<>();
		this.securityHelper = new SecurityHelper();
		
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
	public PublicKey getPublicKeyFor(String roleName, Map<String, Object> arguments) throws KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, CertificateEncodingException, SecurityException, SignatureException, IllegalStateException {
		Integer roleKey = getRoleKey(roleName, arguments);
		
		if (!keyStore.containsAlias(roleKey.toString())) {
			generateKeyPairFor(roleKey);
		}
		
		return keyStore.getCertificate(roleKey.toString()).getPublicKey();
	}

	@Override
	public PrivateKey getPrivateKeyFor(String roleName, Map<String, Object> arguments) throws InvalidKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, SecurityException, SignatureException, IllegalStateException {
		Integer roleKey = getRoleKey(roleName, arguments);
		
		if (!keyStore.containsAlias(roleKey.toString())) {
			generateKeyPairFor(roleKey);
		}
		
		return privateKeys.get(roleKey);
	}
	
	@Override
	public PrivateKey getIntegrityPrivateKey() throws InvalidKeyException, CertificateEncodingException, NoSuchAlgorithmException, KeyStoreException, SecurityException, SignatureException, IllegalStateException {
		if (!keyStore.containsAlias(INTEGRITY_KEY.toString())) {
			generateKeyPairFor(INTEGRITY_KEY);
		}
		
		return privateKeys.get(INTEGRITY_KEY);
	}

	@Override
	public PublicKey getIntegrityPublicKey() throws InvalidKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, SecurityException, SignatureException, IllegalStateException {
		if (!keyStore.containsAlias(INTEGRITY_KEY.toString())) {
			generateKeyPairFor(INTEGRITY_KEY);
		}
		
		return keyStore.getCertificate(INTEGRITY_KEY.toString()).getPublicKey();
	}
	
	private void generateKeyPairFor(Integer roleKey) throws NoSuchAlgorithmException, InvalidKeyException, CertificateEncodingException, KeyStoreException, SecurityException, SignatureException, IllegalStateException {
		KeyPair keypair = securityHelper.generateKeyPair();
		
		keyStore.setCertificateEntry(roleKey.toString(), generateCertificate(roleKey, keypair));
		privateKeys.put(roleKey, keypair.getPrivate());
	}
	
	@SuppressWarnings("deprecation")
	private Certificate generateCertificate(Integer roleKey, KeyPair keypair) throws NoSuchAlgorithmException, InvalidKeyException, SecurityException, SignatureException, CertificateEncodingException, IllegalStateException {		
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

	private Integer getRoleKey(String roleName, Map<String, Object> arguments) {
		if (arguments == null) {
			arguments = Collections.emptyMap();
		}
		int hash = roleName.hashCode()+arguments.hashCode();
		return hash;
	}

}
