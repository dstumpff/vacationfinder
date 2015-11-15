
import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import junit.framework.TestCase;

import org.junit.Before;

import com.evdb.javaapi.data.response.GenericResponse;

public class GenericResponseTest extends TestCase {

	@Before
	public void setUp() throws Exception {
	}
	
	public void testAPIResponse() throws Exception {
		 
        JAXBContext jc = JAXBContext.newInstance(GenericResponse.class);
        Unmarshaller um = jc.createUnmarshaller();
        
		String xml = "<response status =\"ok\"></response>";
		
		GenericResponse ar = (GenericResponse)um.unmarshal(new ByteArrayInputStream(xml.getBytes()));
		
		assertEquals(ar.getStatus(), "ok");
	}

}
