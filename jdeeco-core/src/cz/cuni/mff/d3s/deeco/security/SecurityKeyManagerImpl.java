package cz.cuni.mff.d3s.deeco.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyPair;
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
 * The security key manager.
 *
 * @author Ondřej Štumpf
 */
@SuppressWarnings("deprecation")
public class SecurityKeyManagerImpl implements SecurityKeyManager {

	/** storage of the certificates */
	private KeyStore keyStore;
	
	/** storage of the private keys */
	private Map<Integer, PrivateKey> privateKeys;
	
	/** mapping between role hash keys and actual role values */
	private Map<Integer, RoleWithArguments> issuedRoleKeys;
	
	/** secure random number generator */
	private SecureRandom secureRandom;
	
	/** Security helper instance */
	private SecurityHelper securityHelper;
	
	private final Integer INTEGRITY_KEY = 95423814;
	
	/** Password for the keystore is blank, password for the private key corresponding with the CA certificate is Pa55w0rd */
	private final String CERTIFICATION_AUTHORITY_ALIAS = "CA";
	private final String KEYSTORE_PATH = "../../keystore/keystore.jks";
	
	/** singleton instance */
	private static SecurityKeyManager instance;
	
	/**
	 * Instantiates a new security key manager.
	 *
	 */
	protected SecurityKeyManagerImpl() throws KeyStoreException {
		this.keyStore = KeyStore.getInstance("JKS");
		this.secureRandom = new SecureRandom();
		this.privateKeys = new HashMap<>();
		this.securityHelper = new SecurityHelper();
		this.issuedRoleKeys = new HashMap<>();
		
		try {
			initialize();
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new KeyStoreException(e);
		}
		
	}
	
	/**
	 * Gets the unique instance of this class.
	 * @return
	 * @throws KeyStoreException
	 */
	public static synchronized SecurityKeyManager getInstance() throws KeyStoreException {
		if (instance == null) {
			instance = new SecurityKeyManagerImpl();
		}
		return instance;
	}
	
	private void initialize() throws NoSuchAlgorithmException, CertificateException, IOException {
		String base = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();

		InputStream stream = new FileInputStream(new File(base, KEYSTORE_PATH));
		keyStore.load(stream, null);
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.security.SecurityKeyManager#getPublicKey(java.lang.String, java.util.Map)
	 */
	@Override
	public PublicKey getPublicKey(String roleName, Map<String, Object> arguments) throws KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, CertificateEncodingException, SecurityException, SignatureException, IllegalStateException {
		Integer roleKey = getRoleKey(roleName, arguments);
		
		if (!keyStore.containsAlias(roleKey.toString())) {
			generateKeyPairFor(roleKey);
		}
		
		return keyStore.getCertificate(roleKey.toString()).getPublicKey();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.security.SecurityKeyManager#getPrivateKey(java.lang.String, java.util.Map)
	 */
	@Override
	public PrivateKey getPrivateKey(String roleName, Map<String, Object> arguments) throws InvalidKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, SecurityException, SignatureException, IllegalStateException {
		Integer roleKey = getRoleKey(roleName, arguments);
		
		if (!keyStore.containsAlias(roleKey.toString())) {
			generateKeyPairFor(roleKey);
		}
		
		return privateKeys.get(roleKey);
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.security.SecurityKeyManager#getIntegrityPrivateKey()
	 */
	@Override
	public PrivateKey getIntegrityPrivateKey() throws InvalidKeyException, CertificateEncodingException, NoSuchAlgorithmException, KeyStoreException, SecurityException, SignatureException, IllegalStateException {
		if (!keyStore.containsAlias(INTEGRITY_KEY.toString())) {
			generateKeyPairFor(INTEGRITY_KEY);
		}
		
		return privateKeys.get(INTEGRITY_KEY);
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.security.SecurityKeyManager#getIntegrityPublicKey()
	 */
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

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.security.SecurityKeyManager#getRoleByKey(java.lang.Integer)
	 */
	@Override
	public RoleWithArguments getRoleByKey(Integer roleKey) {
		return issuedRoleKeys.get(roleKey);
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.security.SecurityKeyManager#getRoleKey(java.lang.String, java.util.Map)
	 */
	@Override
	public Integer getRoleKey(final String roleName, Map<String, Object> arguments) {		
		if (arguments == null) {
			arguments = Collections.emptyMap();
		}
		
		final int prime = 31;
		int result = 1;
		result = prime * result + (roleName == null ? 0 : roleName.hashCode());
		result = prime * result + (arguments == null ? 0 : arguments.hashCode());
		
		if (!issuedRoleKeys.containsKey(result)) {
			issuedRoleKeys.put(result, new RoleWithArguments(roleName, arguments));
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.security.SecurityKeyManager#getAuthorityPublicKey()
	 */
	@Override
	public PublicKey getAuthorityPublicKey() throws InvalidKeyException,
			CertificateEncodingException, KeyStoreException,
			NoSuchAlgorithmException, SecurityException, SignatureException,
			IllegalStateException {
		Certificate authorityCertificate = keyStore.getCertificate(CERTIFICATION_AUTHORITY_ALIAS);
		if (authorityCertificate == null) throw new KeyStoreException("Authority certificate not found.");
		
		return authorityCertificate.getPublicKey();
	}

	
}
