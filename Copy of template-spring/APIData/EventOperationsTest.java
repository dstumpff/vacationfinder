
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
public class EventOperationsTest {
	
	private static final String TEST_SEID = "E0-001-003420860-6";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		APIConfigurationData.loadAPIConfigurationDataFromPropertyFile();
	}
	
	/**
	 * Test property functions
	 * @throws Exception
	 */
	@Test
	public void testProperties() throws Exception {
		EventOperations eo = new EventOperations();
		
		Property p = new Property("test name", "test value");
		
		eo.addProperty(TEST_SEID, p);
		
		List<Property> newList = eo.getProperties(TEST_SEID);
		
		assert(newList.size() > 0);
		
		boolean isFound = false;
		for (Property property : newList) {
			if (property.getName().equals("test name")) {
				isFound = true;
				eo.deleteProperty(TEST_SEID, property);
				break;
			}
		}
		if (!isFound) {
			fail("Property not found");
		}
		
		newList = eo.getProperties(TEST_SEID);
		for (Property property : newList) {
			if (property.getName().equals("test name")) {
				fail("Property delete failed");
			}
		}
	}

	
	/**
	 * Test performer functions
	 * @throws Exception
	 */
	@Test
	public void testPerformers() throws Exception {
		EventOperations eo = new EventOperations();
		
		Performer p = new Performer();
		p.setSpid("P0-001-000005422-2");
		
		eo.addPerformer(TEST_SEID, p);
		
		Event e = eo.get(TEST_SEID);
		List<Performer> lc = e.getPerformers();
		
		boolean isPerformerFound = false;
		for (Performer lo : lc) {
			if (lo.getSpid().equals("P0-001-000005422-2")) {
				isPerformerFound = true;
				eo.deletePerformer(TEST_SEID, lo);
			}
		}
		if (!isPerformerFound) {
			fail("Performer not found");
		}
		
		e = eo.get(TEST_SEID);
		lc = e.getPerformers();

		for (Performer lo : lc) {
			if (lo.getSpid().equals("P0-001-000005422-2")) {
				fail("Performers delete failed");
			}
		}
	}
	
	/**
	 * Test our link functionality
	 * @throws Exception
	 */
	@Test
	public void testLinks() throws Exception {
		EventOperations eo = new EventOperations();
		
		Link l = new Link();
		l.setUrl("http://www.test.com/");
		l.setDescription("My description");
		l.setType(Link.LinkType.INFO);
		
		eo.addLink(TEST_SEID, l);
		
		Event e = eo.get(TEST_SEID);
		List<Link> lc = e.getLinks();
		
		boolean isLinkFound = false;
		for (Link lo : lc) {
			if (lo.getUrl().equals("http://www.test.com/")) {
				isLinkFound = true;
				eo.deleteLink(lo);
			}
		}
		if (!isLinkFound) {
			fail("Link not found");
		}
		
		e = eo.get(TEST_SEID);
		lc = e.getLinks();

		for (Link lo : lc) {
			if (lo.getUrl().equals("http://www.test.com/")) {
				fail("Link delete failed");
			}
		}
	}


	/**
	 * Test comment routine
	 * @throws Exception
	 */
	@Test
	public void testComments() throws Exception {
		EventOperations eo = new EventOperations();
		
		Comment c = new Comment("test comment");
		eo.addComment(TEST_SEID, c);
		
		Event e = eo.get(TEST_SEID);
		List<Comment> lc = e.getComments();
		
		boolean isCommentFound = false;
		for (Comment co : lc) {
			if (co.getText().equals("test comment")) {
				isCommentFound = true;
				eo.deleteComment(co);
			}
		}
		if (!isCommentFound) {
			fail("Comment not found");
		}
		
		e = eo.get(TEST_SEID);
		lc = e.getComments();

		for (Comment co : lc) {
			if (co.getText().equals("test comment")) {
				fail("Comment delete failed");
			}
		}
		
		
	}

	/**
	 * Test tag operations
	 * @throws Exception
	 */
	@Test
	public void testTags() throws Exception {
		EventOperations eo = new EventOperations();
		
		Tag t = new Tag("\"junit tag\"");
		List<Tag> tagList = new ArrayList();
		tagList.add(t);
		
		eo.addTags(TEST_SEID, tagList);
		
		List<Tag> newList = eo.getTags(TEST_SEID);
		
		assert(newList.size() > 0);
		
		boolean isTagFound = false;
		for (Tag tag : newList) {
			if (tag.getTitle().equals("junit tag")) {
				isTagFound = true;
				break;
			}
		}
		if (!isTagFound) {
			fail("Tag not found");
		}
		
		eo.deleteTags(TEST_SEID, tagList);
		newList = eo.getTags(TEST_SEID);
		for (Tag tag : newList) {
			if (tag.getTitle().equals("junit tag")) {
				fail("Tag delete failed");
			}
		}
	}

	
	/**
	 * Test method for {@link com.evdb.javaapi.operations.EventOperations#search(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testSearch() throws Exception {
		EventOperations eo = new EventOperations();
		EventSearchRequest esr = new EventSearchRequest();
		esr.setKeywords("music");
		esr.setLocation("San Diego");
		
		SearchResult sr = eo.search(esr);
		
		assert(sr.getTotalItems() > 1);
		List<Event> events = sr.getEvents();
		
		assertNotNull(events.get(0).getSeid());
		assertNotNull(events.get(0).getVenue());
	}
	
	/**
	 * Test method for {@link com.evdb.javaapi.operations.EventOperations#create(java.lang.String)}.
	 * @throws Exception 
	 */
	@Ignore
	public void testCreate() throws Exception {
		EventOperations eo = new EventOperations();
		
		Calendar calStart = Calendar.getInstance();
		
		calStart.set(2007, 11, 22, 15, 15);
		
		Event e = new Event();
		e.setTitle("Tyler test event");
		e.setDescription("An API Test Event");
		e.setFree(true);
		e.setStartTime(calStart.getTime());;
		
		List<Tag> tagList = new ArrayList();
		
		tagList.add(new Tag("ttest9"));
		tagList.add(new Tag("music"));
		
		e.setTags(tagList);

		Event newEvent = eo.create(e);
		
		assertNotNull(newEvent.getSeid());
	}
	
	/**
	 * Test method for {@link com.evdb.javaapi.operations.EventOperations#withdraw(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testWithdraw() throws Exception {
		EventOperations eo = new EventOperations();
		eo.withdraw(TEST_SEID, "Event withdrawn");
		Event e = eo.get(TEST_SEID);
		
		assert(e.isWithdrawn());
		assertEquals(e.getWithdrawnNote(), "Event withdrawn");
		
		eo.restore(TEST_SEID);
		
		e = eo.get(TEST_SEID);
		assert(!e.isWithdrawn());
	}
	
	/**
	 * Test method for {@link com.evdb.javaapi.operations.EventOperations#modify(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testModify() throws Exception {
		EventOperations eo = new EventOperations();
		Event e = eo.get(TEST_SEID);
		
		e.setTitle("Tyler New Title ");
		eo.modify(e);
		e = eo.get(TEST_SEID);
		
		assertEquals(e.getTitle(), "Tyler New Title");
	}

	/**
	 * Test method for {@link com.evdb.javaapi.operations.EventOperations#get(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testGet() throws Exception {
		EventOperations eo = new EventOperations();
		Event e = eo.get("E0-001-002940742-5");
		
		assertEquals(e.getVenue().getName(), "Belly Up");
		assertEquals(e.getLinks().get(0).getId(), 5741099);
		assertEquals(e.getImages().size(), 3);
		assertEquals(e.getImages().get(0).getSmall().getHeight(), 72);
		assertEquals(e.getPerformers().get(0).getSpid(), "P0-001-000005422-2");
		assertEquals(e.getPerformers().get(0).getName(), "Leon Russell");
		assertEquals(e.getTags().get(0).getId(), "bands");
		
		//ensure we get an exception on a bad SEID
		try {
			e = eo.get("E0-001-002940742-9");
			fail("No exception on bad SEID lookup");
		}
		catch (EVDBAPIException e1) {
			//do nothing, we passed
		} catch (Exception e2) {
			fail("Unexpected exception type failure");
			e2.printStackTrace();
		}
	}

}
