/**
 * 
 */


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import junit.framework.TestCase;

import org.junit.Before;

import com.evdb.javaapi.data.Event;
import com.evdb.javaapi.data.Link;

/**
 * @author tylerv
 *
 */
public class EventXMLTest extends TestCase {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	public void testEventGetFromFile1() throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Event.class);
        Unmarshaller um = jc.createUnmarshaller();
        FileInputStream fis = new FileInputStream(new File("test" + File.separator + "resources" + File.separator + "eventsget1.xml"));
        Event e = (Event)um.unmarshal(fis);

		assertEquals(e.getSeid(), "E0-001-003111144-2");
		assertEquals(e.getGroups().size(), 1);
		assertEquals(e.getGoing().size(), 3);
		assertEquals(e.getCategories().get(0).getId(), "technology");
		assertEquals(e.getTags().get(0).getId(), "barcamp");
		
	}

	public void testEventGetFromFile2() throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Event.class);
        Unmarshaller um = jc.createUnmarshaller();
        FileInputStream fis = new FileInputStream(new File("test" + File.separator + "resources" + File.separator + "eventsget2.xml"));
        Event e = (Event)um.unmarshal(fis);

		assertEquals(e.getVenue().getName(), "Belly Up");
		assertEquals(e.getLinks().get(0).getId(), 5741099);
		assertEquals(e.getImages().size(), 3);
		assertEquals(e.getImages().get(0).getSmall().getHeight(), 72);
		assertEquals(e.getPerformers().get(0).getSpid(), "P0-001-000005422-2");
		assertEquals(e.getPerformers().get(0).getName(), "Leon Russell");
		assertEquals(e.getTags().get(0).getId(), "bands");
		
	}

	/**
	 * Simple marshal/unmarshal test
	 * @throws Exception
	 */
	public void testEventGetFromString() throws Exception {
		
        JAXBContext jc = JAXBContext.newInstance(Event.class);
        
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        Event e1 = new Event();
		e1.setSvid("12345");
		
		e1.setVenueAddress("123 Main St");
		
		e1.setVenueLatitude( 213.123);
		e1.setStartTime(Calendar.getInstance().getTime());
		e1.setSeid("E-123-4566");
		e1.setTitle ("test event");
		e1.setVenueName ("test venue");
		
		Link l = new Link();
		l.setId(123);
		l.setDescription("test link");
		l.setUrl("http://www.blah.com/");
		
		ArrayList<Link> ll = new ArrayList();
		ll.add(l);
		e1.setLinks( ll);
		m.marshal(e1, System.out);
		
		String xml = "<event id=\"E-123-456\">" +
				"<title>test title</title>" +
				"<venue_name>Test Venue</venue_name>" +
				"<start_time>2007-05-25 00:00:00</start_time>" +
				"<venue_id>V-123-3455</venue_id>" +
				"<all_day>1</all_day>" +
				"<latitude>123.1241</latitude>" +
				"<geocode_type>City Based</geocode_type>" +
				
				"<links>" + 
				"<link id=\"456\">" +
				"<description>Link Description</description>" +
				"<url>http://www.mylink.com</url>" +
				"</link>" +
				"</links>" +

				"<performers>" + 
				"<performer id=\"P-01-456\">" +
				"<name>Test Performer</name>" +
				"<short_bio>Test Performer Bio</short_bio>" +
				"</performer>" +
				"</performers>" +
				
				
				"</event>";
		
        Unmarshaller um = jc.createUnmarshaller();
        Event e = (Event)um.unmarshal(new ByteArrayInputStream(xml.getBytes()));
		
		assertEquals(e.getSeid(), "E-123-456");
		assertEquals(e.isAllDay(), true);
		assertEquals(e.getVenue().getName(), "Test Venue");
		assertEquals(e.getVenue().getLatitude(), 123.1241);
		assertEquals(e.getLinks().get(0).getUrl(), "http://www.mylink.com");
		assertEquals(e.getPerformers().get(0).getShortBio(), "Test Performer Bio");
	}

}
