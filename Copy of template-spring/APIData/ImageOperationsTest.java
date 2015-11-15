/**
 * 
 */
import java.io.File;

import org.junit.Before;
import org.junit.Ignore;

import com.evdb.javaapi.EVDBAPIException;
import com.evdb.javaapi.EVDBRuntimeException;
import com.evdb.javaapi.operations.ImageOperations;

/**
 * @author tylerv
 *
 */
public class ImageOperationsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		APIConfigurationData.loadAPIConfigurationDataFromPropertyFile();
	}

	/**
	 * Test method for {@link com.evdb.javaapi.operations.ImageOperations#uploadImageFromURL(java.lang.String, java.lang.String)}.
	 * @throws EVDBAPIException 
	 * @throws EVDBRuntimeException 
	 */
	@Ignore
	public void testAddImageFromURL() throws EVDBRuntimeException, EVDBAPIException {
		ImageOperations io = new ImageOperations();
		String id = io.uploadImageFromURL("http://www.flower-garden-bulbs.com/images/dougnew.jpg", "Doug the flower guy");
		assert(id.length() > 0);
	}

	/**
	 * Test method for {@link com.evdb.javaapi.operations.ImageOperations#uploadImageFromLocalFile(java.io.File, java.lang.String)}.
	 * @throws EVDBAPIException 
	 * @throws EVDBRuntimeException 
	 */
	@Ignore
	public void testAddImageFromLocalFile() throws EVDBRuntimeException, EVDBAPIException {
		ImageOperations io = new ImageOperations();
		String id = io.uploadImageFromLocalFile(new File("/home/tylerv/projects/evdb/javaapi/test/resources/flower.jpg"), "Doug the flower guy");
		assert(id.length() > 0);
	}

}
