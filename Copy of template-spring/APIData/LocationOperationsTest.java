import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.evdb.javaapi.data.Location;
import com.evdb.javaapi.operations.LocationOperations;

public class LocationOperationsTest {

	@Before
	public void setUp() throws Exception {
		APIConfigurationData.loadAPIConfigurationDataFromPropertyFile();
	}
	
	/**
	 * Test search
	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception {
		LocationOperations lo = new LocationOperations();
		Location l = lo.search("Los Angeles");
		
		assertEquals(l.getRegion(), "California");
		assertEquals(l.getMetro(), "Los Angeles");
		
		l = lo.search("92109");
		
		assertEquals(l.getRegion(), "California");
		assertEquals(l.getMetro(), "San Diego");
		assertEquals(l.getPostalCode(), "92109");
		
		assertEquals(l.getLongitude(), -117.236);
		
		l = lo.search("asasdasda");
		assertEquals(l, null);
		
		
	}


}
