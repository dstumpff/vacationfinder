import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.evdb.javaapi.APIConfiguration;
import com.evdb.javaapi.EVDBAPIException;
import com.evdb.javaapi.EVDBRuntimeException;
import com.evdb.javaapi.data.Comment;
import com.evdb.javaapi.data.Demand;
import com.evdb.javaapi.data.Link;
import com.evdb.javaapi.data.Performer;
import com.evdb.javaapi.data.SearchResult;
import com.evdb.javaapi.data.Tag;
import com.evdb.javaapi.data.request.DemandSearchRequest;
import com.evdb.javaapi.operations.DemandOperations;
import com.evdb.javaapi.operations.PerformerOperations;

/**
 * Test methods for demand objects
 * @author tylerv
 *
 */
public class DemandOperationsTest {

	private static final String TEST_SDID = "D0-001-000045638-7";
	private static final String TEST_SPID = "P0-001-000002882-9";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		APIConfigurationData.loadAPIConfigurationDataFromPropertyFile();
	}

	/**
	 * Test method for {@link com.evdb.javaapi.operations.DemandOperations#get(java.lang.String)}.
	 * @throws EVDBAPIException 
	 * @throws EVDBRuntimeException 
	 */
	@Test
	public final void testGet() throws EVDBRuntimeException, EVDBAPIException {
		DemandOperations dop = new DemandOperations();
		Demand d = dop.get(TEST_SDID);
		
		assertEquals(d.getSdid(), TEST_SDID);
		assertEquals(d.getPerformer().getSpid(), TEST_SPID);
		assertEquals(d.getPerformer().getName(), "Pretty Ricky");
		assert(d.getTags().size() > 0);
		assert(d.getImages().size() > 0);
		assert(d.getComments().size() > 0);
		assert(d.getLinks().size() > 0);
	}

	/**
	 * Test method for {@link com.evdb.javaapi.operations.DemandOperations#create(com.evdb.javaapi.data.Demand)}.
	 * @throws EVDBAPIException 
	 * @throws EVDBRuntimeException 
	 */
	@Ignore
	public final void testCreate() throws EVDBRuntimeException, EVDBAPIException {
		DemandOperations dop = new DemandOperations();
		PerformerOperations po = new PerformerOperations();
		
		Demand d = new Demand();
		
		Performer p = po.get(TEST_SPID);
		
		d.setPerformer(p);
		d.setLocation("Guelph, Ontario, Canada");
		d.setDescription("Go guelph!");
		
		d = dop.create(d);
		
		assert(d.getSdid().length() > 0);
		
		Demand d2 = dop.get(d.getSdid());
		assertEquals(d2.getPerformer().getSpid(), TEST_SPID);
		assertEquals(d2.getDescription(), "Go guelph!");
		
	}

	/**
	 * Test method for {@link com.evdb.javaapi.operations.DemandOperations#modify(com.evdb.javaapi.data.Demand)}.
	 * @throws EVDBAPIException 
	 * @throws EVDBRuntimeException 
	 */
	@Ignore
	public final void testModify() throws EVDBRuntimeException, EVDBAPIException {
		DemandOperations dop = new DemandOperations();
		Demand d = dop.get(TEST_SDID);
		
		d.setDescription("This is a test description");
		
		dop.modify(d);
		
		d = dop.get(TEST_SDID);
		
		
		assertEquals(d.getSdid(), TEST_SDID);
		assertEquals(d.getDescription(), "This is a test description");
		assertEquals(d.getPerformer().getSpid(), TEST_SPID);
		assertEquals(d.getPerformer().getName(), "Pretty Ricky");
		assert(d.getTags().size() > 0);
		assert(d.getImages().size() > 0);
		assert(d.getComments().size() > 0);
		assert(d.getLinks().size() > 0);
	}

	/**
	 * Test our link functionality
	 * @throws Exception
	 */
	@Test
	public void testLinks() throws Exception {
		DemandOperations dop = new DemandOperations();
		
		Link l = new Link();
		l.setUrl("http://www.test.com/");
		l.setDescription("My description");
		l.setType(Link.LinkType.INFO);
		
		dop.addLink(TEST_SDID, l);
		
		Demand e = dop.get(TEST_SDID);
		List<Link> lc = e.getLinks();
		
		boolean isLinkFound = false;
		for (Link lo : lc) {
			if (lo.getUrl().equals("http://www.test.com/")) {
				isLinkFound = true;
				dop.deleteLink(lo);
			}
		}
		if (!isLinkFound) {
			fail("Link not found");
		}
		
		e = dop.get(TEST_SDID);
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
		DemandOperations dop = new DemandOperations();
		
		Comment c = new Comment("test comment");
		dop.addComment(TEST_SDID, c);
		
		Demand e = dop.get(TEST_SDID);
		List<Comment> lc = e.getComments();
		
		boolean isCommentFound = false;
		for (Comment co : lc) {
			if (co.getText().equals("test comment")) {
				isCommentFound = true;
				dop.deleteComment(co);
			}
		}
		if (!isCommentFound) {
			fail("Comment not found");
		}
		
		e = dop.get(TEST_SDID);
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
		DemandOperations dop = new DemandOperations();
		
		Tag t = new Tag("\"junit tag\"");
		List<Tag> tagList = new ArrayList();
		tagList.add(t);
		
		dop.addTags(TEST_SDID, tagList);
		
		List<Tag> newList = dop.getTags(TEST_SDID);
		
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
		
		dop.deleteTags(TEST_SDID, tagList);
		newList = dop.getTags(TEST_SDID);
		for (Tag tag : newList) {
			if (tag.getTitle().equals("junit tag")) {
				fail("Tag delete failed");
			}
		}
	}

	/** 
	 * Test method for {@link com.evdb.javaapi.operations.DemandOperations#search(com.evdb.javaapi.data.request.DemandSearchRequest)}.
	 * @throws EVDBAPIException 
	 * @throws EVDBRuntimeException 
	 */
	@Test
	public final void testSearch() throws EVDBRuntimeException, EVDBAPIException {
		DemandOperations dop = new DemandOperations();
		DemandSearchRequest psr = new DemandSearchRequest();
		psr.setKeywords("pretty ricky");
		
		SearchResult sr = dop.search(psr);
		
		assert(sr.getTotalItems() > 1);
		List<Demand> demands = sr.getDemands();
		
		assertNotNull(demands.get(0).getSdid());
	}

}
