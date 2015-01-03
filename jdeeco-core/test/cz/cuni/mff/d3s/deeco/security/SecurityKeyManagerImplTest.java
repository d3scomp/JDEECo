package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import org.junit.Before;
import org.junit.Test;

public class SecurityKeyManagerImplTest {

	private SecurityKeyManagerImpl target;
	
	@Before
	public void setUp() throws KeyStoreException {
		target = new SecurityKeyManagerImpl();
	}
	
	@Test
	public void getPublicKeyTest() {
		
	}
}
