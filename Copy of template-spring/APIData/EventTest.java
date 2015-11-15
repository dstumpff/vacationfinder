/**
 * 
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.evdb.javaapi.EVDBAPIException;
import com.evdb.javaapi.data.Comment;
import com.evdb.javaapi.data.Event;
import com.evdb.javaapi.data.Image;
import com.evdb.javaapi.data.Link;
import com.evdb.javaapi.data.Performer;
import com.evdb.javaapi.data.Property;
import com.evdb.javaapi.data.SearchResult;
import com.evdb.javaapi.data.Tag;
import com.evdb.javaapi.data.request.EventSearchRequest;
import com.evdb.javaapi.operations.EventOperations;

/**
 * @author tylerv
 *
 */
public class EventTest {
	
	private static final String TEST_SEID = "E0-001-003420860-6";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		APIConfigurationData.loadAPIConfigurationDataFromPropertyFile();
	}
	
	/**
	 * Test method for {@link com.evdb.javaapi.operations.EventOperations#search(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testSearch() throws Exception {
		EventOperations eo = new EventOperations();
		EventSearchRequest esr = new EventSearchRequest();
		esr.setKeywords("has_images:1");
		esr.setLocation("San Diego");
		
		SearchResult sr = eo.search(esr);
		
		assert(sr.getTotalItems() > 1);
		List<Event> events = sr.getEvents();
		
		assertNotNull(events.get(0).getSeid());
		assertNotNull(events.get(0).getVenue());
	
		Event e = events.get(0);
		List<Image> images = e.getImages();
		Image image = images.get(0);
		String url = image.getUrl();
		System.err.println(e.getSeid() + " :: Image: " + url + " size(" + images.size() + ")");
		assertNotNull(url);
	}
	
	/**
	 * Test method for {@link com.evdb.javaapi.operations.EventOperations#get(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testGet() throws Exception {
		EventOperations eo = new EventOperations();
		Event e = eo.get("E0-001-002940742-5");
		
		List<Image> images = e.getImages();
		Image image = images.get(0);
		String url = image.getUrl();
		System.err.println(e.getSeid() + " :: Image: " + url + " size(" + images.size() + ")");
		assertNotNull(url);
		assertEquals(
			"url",
			"http://eventful.com/solanabeach/events/leon-russell-w-diablo-dimes-/E0-001-002940742-5?utm_source=apis&utm_medium=apim&utm_campaign=apic",
			e.getURL()
		);
	}

}
