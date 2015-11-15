import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.evdb.javaapi.APIConfiguration;

/**
 * Load the API configuration for the JUnit tests from a file
 * @author tylerv
 *
 */
public class APIConfigurationData {
	
	public static void loadAPIConfigurationDataFromPropertyFile() {
		Properties properties = new Properties();
	    try {
	        properties.load(new FileInputStream("api.properties"));
	        
	        APIConfiguration.setApiKey(properties.getProperty("key"));
			APIConfiguration.setEvdbUser(properties.getProperty("user"));
			APIConfiguration.setEvdbPassword(properties.getProperty("password"));
			APIConfiguration.setBaseURL(properties.getProperty("baseurl", "http://api.eventful.com/rest/"));
	        
	    } catch (IOException e) {
	    	System.err.println("Error: JUNIT api.properties file not found");
	    	e.printStackTrace();
	    }
	}

}
