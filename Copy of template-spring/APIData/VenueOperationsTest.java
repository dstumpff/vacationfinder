import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.evdb.javaapi.data.SearchResult;
import com.evdb.javaapi.data.Venue;
import com.evdb.javaapi.data.request.VenueSearchRequest;
import com.evdb.javaapi.operations.VenueOperations;

/**
 * @author tylerv
 *
 */
public class VenueOperationsTest {
	
	private static final String TEST_SVID = "V0-001-000106394-4";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		APIConfigurationData.loadAPIConfigurationDataFromPropertyFile();
	}
	
	/**
	 * Test modify
	 * @throws Exception
	 */
	@Test
	public void testModify() throws Exception {
		VenueOperations vo = new VenueOperations();
		Venue v = vo.get(TEST_SVID);
		
		v.setDescription("New Description");
		vo.modify(v);
		v = vo.get(TEST_SVID);
		
		assertEquals(v.getDescription(), "New Description");
	}
	
	/**
	 * Test search
	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception {
		VenueOperations vo = new VenueOperations();
		VenueSearchRequest esr = new VenueSearchRequest();
		esr.setKeywords("San Diego Civic Theatre");
		esr.setLocation("San Diego");
		
		SearchResult sr = vo.search(esr);
		
		assert(sr.getTotalItems() > 1);
		List<Venue> venues = sr.getVenues();
		
		assertNotNull(venues.get(0).getSvid());
		assertNotNull(venues.get(0).getCity());
	}
	
	/**
	 * Test Get
	 * @throws Exception
	 */
	@Test
	public void testGet() throws Exception {
		VenueOperations vo = new VenueOperations();
		Venue v = vo.get(TEST_SVID);
		
		assertEquals(v.getName(), "San Diego Civic Theatre");
		assertEquals(v.getSvid(), TEST_SVID);
		assertEquals(v.getCity(), "San Diego");
		assertEquals(v.getCountryThreeLetterAbbreviation(), "USA");
		assertEquals(v.getCity(), "San Diego");
		assert(v.getEvents().size() > 0);
		assertEquals(v.getDisplay(), 1);
	}

}
