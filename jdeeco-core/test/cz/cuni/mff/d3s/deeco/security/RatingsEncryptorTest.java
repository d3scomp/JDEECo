package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SealedObject;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.integrity.PathRating;
import cz.cuni.mff.d3s.deeco.integrity.RatingsChangeSet;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.network.RatingsMetaData;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RatingsEncryptorTest {

	private RatingsEncryptor target;	
	private SecurityKeyManager keyManager;
	private List<RatingsChangeSet> ratings;
	private RatingsMetaData ratingsMetaData;
	
	@Before
	public void setUp() throws KeyStoreException {
		keyManager = new SecurityKeyManagerImpl();
		target = new RatingsEncryptor(keyManager);
		
		ratings = new ArrayList<>();
		ratings.add(new RatingsChangeSet("a", "bsfsdf", RuntimeModelHelper.createKnowledgePath("field"), null));
		ratings.add(new RatingsChangeSet("bsfsdf", "x", RuntimeModelHelper.createKnowledgePath("field", "x", "y"), PathRating.OUT_OF_RANGE));
		ratingsMetaData = new RatingsMetaData(123, 456);
	}
	
	@Test
	public void encryptTest() {
		// given ratings data are prepared		
		
		// when the data are encrypted
		List<SealedObject> sealedRatings = target.encryptRatings(ratings, ratingsMetaData);
		
		// then the keys are set
		assertNotNull(ratingsMetaData.encryptedKey);
		assertNotNull(ratingsMetaData.encryptedKeyAlgorithm);
		
		// then data are encrypted
		assertEquals(2, sealedRatings.size());
	}
	
	@Test
	public void decryptTest() {
		// given ratings data are prepared		
		
		// when the data are encrypted
		List<SealedObject> sealedRatings = target.encryptRatings(ratings, ratingsMetaData);
		
		// when the data are decrypted
		List<RatingsChangeSet> decryptedRatings = target.decryptRatings(sealedRatings, ratingsMetaData);
		
		// then the data are equal
		assertEquals(ratings, decryptedRatings);
	}
}
