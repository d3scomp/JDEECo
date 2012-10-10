/*
 * This file is committed for reference when building testNG tests.
 * It can be deleted any time without problem.
 * 
 */
package cz.cuni.mff.d3s.deeco.test.unit;

import static org.testng.Assert.assertEquals;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestNGExamples {

	@BeforeClass
	 public void setUp() {
		System.out.println("instantiating Class");
	 }
	
	@Test(groups = { "fast" })
	public void aFastTest() {
	  System.out.println("Fast test");
	}
 
	@Test(groups = { "slow" })
	public void aSlowTest() {
	   System.out.println("Slow test");
	}
	 
	@Test(expectedExceptions = NullPointerException.class)
    public void testNullPointerException() {
		assertEquals(true,true);
        List<?> list = null;
        list.size();
    }
	 
	//set timeout in ms:
    @Test(timeOut = 1000, enabled=false)
    public void waitLongTime() throws Exception {
        Thread.sleep(1001);
    }
    
    @DataProvider
	private static final Object[][] getMoney(){
	    return new Object[][] {
	        {new Integer(4), new Integer(3), 7},
	        {new Integer(1), new Integer(4), 5},
	        {new Integer(1234), new Integer(234), 1468}};
	}
	    
	@Test(dataProvider = "getMoney", dependsOnMethods = { "testNullPointerException" })
	public void shouldAddSameCurrencies(Integer a, Integer b, 
	        Integer expectedResult) {
		Integer result = a + b;
	    assertEquals(result, expectedResult);
	}
    
}
