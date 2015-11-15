import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.evdb.javaapi.EVDBAPIException;
import com.evdb.javaapi.EVDBRuntimeException;
import com.evdb.javaapi.data.Comment;
import com.evdb.javaapi.data.Link;
import com.evdb.javaapi.data.Performer;
import com.evdb.javaapi.data.Property;
import com.evdb.javaapi.data.SearchResult;
import com.evdb.javaapi.data.Tag;
import com.evdb.javaapi.data.request.PerformerSearchRequest;
import com.evdb.javaapi.operations.PerformerOperations;

/**
 * @author tylerv
 *
 */
public class PerformerOperationsTest {
	
	private static final String TEST_SPID = "P0-001-000002882-9";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		APIConfigurationData.loadAPIConfigurationDataFromPropertyFile();
	}

	/**
	 * Test method for {@link com.evdb.javaapi.operations.PerformerOperations#get(java.lang.String)}.
	 * @throws EVDBAPIException 
	 * @throws EVDBRuntimeException 
	 */
	@Test
	public final void testGet() throws EVDBRuntimeException, EVDBAPIException {
		PerformerOperations po = new PerformerOperations();
		Performer p = po.get(TEST_SPID);
		
		assertEquals(p.getSpid(), TEST_SPID);
		assertEquals(p.getName(), "Pretty Ricky");
		assert(p.getTags().size() > 0);
		assert(p.getImages().size() > 0);
		assert(p.getComments().size() > 0);
		assert(p.getLinks().size() > 0);
	}

	/**
	 * Test method for {@link com.evdb.javaapi.operations.PerformerOperations#create(com.evdb.javaapi.data.Performer)}.
	 * @throws EVDBAPIException 
	 * @throws EVDBRuntimeException 
	 */
	@Ignore
	public final void testCreate() throws EVDBRuntimeException, EVDBAPIException {
		PerformerOperations po = new PerformerOperations();
		Performer p = new Performer();
		
		p.setName("JUnit Test");
		p.setShortBio("Just a junit test");
		
		p = po.create(p);
		
		assert(p.getSpid().length() > 0);
		
		Performer p2 = po.get(p.getSpid());
		assertEquals(p2.getName(), "JUnit Test");
		assertEquals(p2.getShortBio(), "Just a junit test");
		
	}

	/**
	 * Test method for {@link com.evdb.javaapi.operations.PerformerOperations#modify(com.evdb.javaapi.data.Performer)}.
	 * @throws EVDBAPIException 
	 * @throws EVDBRuntimeException 
	 */
	@Ignore
	public final void testModify() throws EVDBRuntimeException, EVDBAPIException {
		PerformerOperations po = new PerformerOperations();
		Performer p = po.get(TEST_SPID);
		
		p.setShortBio("Hip Hop SuperStars");
		
		po.modify(p);
		
		p = po.get(TEST_SPID);
		
		
		assertEquals(p.getSpid(), TEST_SPID);
		assertEquals(p.getName(), "Pretty Ricky");
		assertEquals(p.getShortBio(), "Hip Hop SuperStars");
		assert(p.getTags().size() > 0);
		assert(p.getImages().size() > 0);
		assert(p.getComments().size() > 0);
		assert(p.getLinks().size() > 0);
	}

	/**
	 * Test our link functionality
	 * @throws Exception
	 */
	@Test
	public void testLinks() throws Exception {
		PerformerOperations po = new PerformerOperations();
		
		Link l = new Link();
		l.setUrl("http://www.test.com/");
		l.setDescription("My description");
		l.setType(Link.LinkType.INFO);
		
		po.addLink(TEST_SPID, l);
		
		Performer e = po.get(TEST_SPID);
		List<Link> lc = e.getLinks();
		
		boolean isLinkFound = false;
		for (Link lo : lc) {
			if (lo.getUrl().equals("http://www.test.com/")) {
				isLinkFound = true;
				po.deleteLink(TEST_SPID,lo);
			}
		}
		if (!isLinkFound) {
			fail("Link not found");
		}
		
		e = po.get(TEST_SPID);
		lc = e.getLinks();

		for (Link lo : lc) {
			if (lo.getUrl().equals("http://www.test.com/")) {
				fail("Link delete failed");
			}
		}
	}
	/**
	 * Test property functions
	 * @throws Exception
	 */
	@Test
	public void testProperties() throws Exception {
		PerformerOperations po = new PerformerOperations();
		
		Property p = new Property("test name", "test value");
		
		po.addProperty(TEST_SPID, p);
		
		List<Property> newList = po.getProperties(TEST_SPID);
		
		assert(newList.size() > 0);
		
		boolean isFound = false;
		for (Property property : newList) {
			if (property.getName().equals("test name")) {
				isFound = true;
				po.deleteProperty(TEST_SPID, property);
				break;
			}
		}
		if (!isFound) {
			fail("Property not found");
		}
		
		newList = po.getProperties(TEST_SPID);
		for (Property property : newList) {
			if (property.getName().equals("test name")) {
				fail("Property delete failed");
			}
		}
	}
	
	/**
	 * Test comment routine
	 * @throws Exception
	 */
	@Test
	public void testComments() throws Exception {
		PerformerOperations po = new PerformerOperations();
		
		Comment c = new Comment("test comment");
		po.addComment(TEST_SPID, c);
		
		Performer e = po.get(TEST_SPID);
		List<Comment> lc = e.getComments();
		
		boolean isCommentFound = false;
		for (Comment co : lc) {
			if (co.getText().equals("test comment")) {
				isCommentFound = true;
				po.deleteComment(co);
			}
		}
		if (!isCommentFound) {
			fail("Comment not found");
		}
		
		e = po.get(TEST_SPID);
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
		PerformerOperations po = new PerformerOperations();
		
		Tag t = new Tag("\"junit tag\"");
		List<Tag> tagList = new ArrayList();
		tagList.add(t);
		
		po.addTags(TEST_SPID, tagList);
		
		List<Tag> newList = po.getTags(TEST_SPID);
		
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
		
		po.deleteTags(TEST_SPID, tagList);
		newList = po.getTags(TEST_SPID);
		for (Tag tag : newList) {
			if (tag.getTitle().equals("junit tag")) {
				fail("Tag delete failed");
			}
		}
	}

	/**
	 * Test method for {@link com.evdb.javaapi.operations.PerformerOperations#search(com.evdb.javaapi.data.request.PerformerSearchRequest)}.
	 * @throws EVDBAPIException 
	 * @throws EVDBRuntimeException 
	 */
	@Test
	public final void testSearch() throws EVDBRuntimeException, EVDBAPIException {
		PerformerOperations po = new PerformerOperations();
		PerformerSearchRequest psr = new PerformerSearchRequest();
		psr.setKeywords("pretty ricky");
		
		SearchResult sr = po.search(psr);
		
		assert(sr.getTotalItems() > 1);
		List<Performer> performers = sr.getPerformers();
		
		assertNotNull(performers.get(0).getSpid());
	}

}
