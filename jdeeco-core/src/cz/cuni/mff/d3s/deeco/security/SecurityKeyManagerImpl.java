package cz.cuni.mff.d3s.deeco.security;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;



public class SecurityKeyManagerImpl implements SecurityKeyManager {

	private KeyStore keyStore;
	private SecureRandom secureRandom;
	
	public SecurityKeyManagerImpl() throws KeyStoreException {
		this.keyStore = KeyStore.getInstance("JKS");
		//this.keyStore.load(stream, password); TODO
		this.secureRandom = new SecureRandom();
	}
	
	@Override
	public Key getPublicKeyFor(String roleName, List<Object> arguments) throws KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, CertificateEncodingException, SecurityException, SignatureException, IllegalStateException {
		String roleKey = getRoleKey(roleName, arguments);
		
		if (!keyStore.containsAlias(roleKey)) {
			keyStore.setCertificateEntry(roleKey, generateCertificate(roleKey));
		}
		
		return keyStore.getCertificate(roleKey).getPublicKey();
	}

	@SuppressWarnings("deprecation")
	private Certificate generateCertificate(String roleKey) throws NoSuchAlgorithmException, InvalidKeyException, SecurityException, SignatureException, CertificateEncodingException, IllegalStateException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024, secureRandom);
        KeyPair keypair = keyGen.generateKeyPair();
        
        X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();
        v3CertGen.setSerialNumber(BigInteger.valueOf(new SecureRandom().nextInt()));
        v3CertGen.setIssuerDN(new X509Principal("CN=local, OU=None, O=None L=None, C=None"));
        v3CertGen.setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30));
        v3CertGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365*10)));
        v3CertGen.setSubjectDN(new X509Principal("CN=" + roleKey + ", OU=None, O=None L=None, C=None"));
        
        v3CertGen.setPublicKey(keypair.getPublic());
        v3CertGen.setSignatureAlgorithm("MD5WithRSAEncryption"); 
        
        return v3CertGen.generate(keypair.getPrivate());
	}

	private String getRoleKey(String roleName, List<Object> arguments) {
		return roleName+"("+arguments.stream().map(o -> o.toString()).collect(Collectors.joining(","))+")";
	}

}
