import com.evdb.javaapi.APIConfiguration;
import com.evdb.javaapi.EVDBAPIException;
import com.evdb.javaapi.EVDBRuntimeException;
import com.evdb.javaapi.data.Comment;
import com.evdb.javaapi.data.Demand;
import com.evdb.javaapi.data.Event;
import com.evdb.javaapi.data.Image;
import com.evdb.javaapi.data.Link;
import com.evdb.javaapi.data.Location;
import com.evdb.javaapi.data.Performer;
import com.evdb.javaapi.data.Property;
import com.evdb.javaapi.data.SearchResult;
import com.evdb.javaapi.data.Tag;
import com.evdb.javaapi.data.Venue;
import com.evdb.javaapi.data.request.DemandSearchRequest;
import com.evdb.javaapi.data.request.EventSearchRequest;
import com.evdb.javaapi.data.request.PerformerSearchRequest;
import com.evdb.javaapi.data.request.VenueSearchRequest;
import com.evdb.javaapi.data.response.GenericResponse;
import com.evdb.javaapi.operations.DemandOperations;
import com.evdb.javaapi.operations.EventOperations;
import com.evdb.javaapi.operations.ImageOperations;
import com.evdb.javaapi.operations.LocationOperations;
import com.evdb.javaapi.operations.PerformerOperations;
import com.evdb.javaapi.operations.VenueOperations;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.List;

public class TestEventful {



public static void main(String[] args) {
// 	public void Run()
//	{ }
		
Properties properties = new Properties();
    //properties.load(new FileInputStream("api.properties"));
    
    APIConfiguration.setApiKey("BznmrDJCddHGGfRJ");
    APIConfiguration.setEvdbUser("aaron_salinas@baylor.edu");
    APIConfiguration.setEvdbPassword("bits please");
    APIConfiguration.setBaseURL("http://api.eventful.com/rest/");
	    EventOperations eo = new EventOperations();
	    EventSearchRequest esr = new EventSearchRequest();

	    esr.setLocation("Redmond, WA, USA");
	    
            // is this correct date range setting? (no documentation found)
            ////////////////////////////////////////////////
//            long start = new Date().getTime();
	    long diff = 1000 * 60 * 60 * 24 * 7;
//	    String daterange = Long.toString(start) + "-" + Long.toString(start+diff);
            ////////////////////////////////////////////////
//	    esr.setDateRange(daterange);
            esr.setDateRange("2012110200-2013052100");
            esr.setIncludes("categories");
	    esr.setPageSize(100);
	    esr.setPageNumber(1);
	    	        
	    SearchResult sr = null;
	    try {
	                sr = eo.search(esr);
	                if (sr.getTotalItems() > 1) 
	                {
	                    int totalitems = sr.getTotalItems();
	                	int pagecount = sr.getPageCount();
	                	int pageitem = sr.getPageItems();
	                	
	                	List<Event> events = sr.getEvents();
	                	for(Event event : events)
	                	{
	                		Venue venue = event.getVenue();  // --> always address properties of a venue are null
	                		String title = event.getTitle();
	                		String seid  = event.getSeid();
	                		String svid  = event.getSvid();
	                		String desc = event.getDescription();
	                		String address = venue.getAddress(); // --> this always returns null
	                		String city = venue.getCity();  // --> this always returns null
	                		String venuename = event.getVenueName();
	                		String path = event.getOlsonPath();
	                		List<Property> porps = event.getProperties();
	                		String region = event.getVenueRegion();
	                	        System.out.println("title: " + title + " venueName: " + venuename + ": " + "seid: " + seid + "svid: " + svid + "address: " + address  + "city: " + city );
	                	        System.out.println("categories : " + event.getCategories() );
	                	}
	                	System.out.println("Total items: " + sr.getTotalItems());
	                } else {
	                	System.out.println("Got no items :( \n");
                        }
	        }catch(EVDBRuntimeException var){
	                System.out.println("Opps got runtime an error...");
	        } catch( EVDBAPIException var){
	                System.out.println("Opps got runtime an error...");
	        }		

	    VenueOperations vo = new VenueOperations();
	    VenueSearchRequest vsr = new VenueSearchRequest();

	    vsr.setLocation("New York , United States");
	    vsr.setSortOrder(VenueSearchRequest.SortOrder.POPULARITY);
	    vsr.setSortDirection(VenueSearchRequest.SortDirection.DESCENDING);
	    try {
	                sr = vo.search(vsr);
	                if (sr.getTotalItems() > 1) 
	                {
	                      	int totalitems = sr.getTotalItems();
	                	int pagecount = sr.getPageCount();
	                	int pageitem = sr.getPageItems();
	                	
	                	List<Venue> venues = sr.getVenues();
	                	for(Venue venue : venues)
	                	{
	                		String address = venue.getAddress(); // --> this always returns null
	                		String city = venue.getCity();  // --> this always returns null
	                		String svid = venue.getSvid();
	                		String venuename = venue.getName();
	                	        System.out.println("svid: " + svid + " venueName: " + venuename + ": " + "address: " + address  + "city: " + city );
	                	}
	                	System.out.println("Total Venue items: " + sr.getTotalItems());
	                } else {
	                	System.out.println("Got no items :( \n");
                        }
	        }catch(EVDBRuntimeException var){
	                System.out.println("Opps got runtime an error...");
	        } catch( EVDBAPIException var){
	                System.out.println("Opps got runtime an error...");
	        }		
	}




public static void main2(String[] args) {


Properties properties = new Properties();
try {
    properties.load(new FileInputStream("api.properties"));

    APIConfiguration.setApiKey(properties.getProperty("key"));
    APIConfiguration.setEvdbUser(properties.getProperty("user"));
    APIConfiguration.setEvdbPassword(properties.getProperty("password"));
    APIConfiguration.setBaseURL(properties.getProperty("baseurl", "http://api.eventful.com/rest/"));

} catch (IOException e) {
    System.err.println("Error: api.properties file not found - you need one to run the program");
    e.printStackTrace();
}



        EventOperations eo = new EventOperations();
        EventSearchRequest esr = new EventSearchRequest();

        esr.setLocation("San Diego");
        esr.setDateRange("2012050200-2013052100");

        //comment next line to see the results without categories
        esr.setCategory("family_fun_kids,conference,books,learning_education,animals,outdoors_recreation,schools_alumni,business,technology,religion_spirituality,sales,food,movies_film,festivals_parades,support,politics_activism,attractions,art,performing_arts,clubs_associations,singles_social,community,other,sports,fundraisers,music,science,fundraisers"); //all categories

        esr.setPageSize(20);
        esr.setPageNumber(1);
//        esr.setConnectionTimeout(60000);
//        esr.setReadTimeout(55123);
//      esr.setSortOrder(SortOrder.DATE);
        SearchResult sr = null;

        try {
                sr = eo.search(esr);
                if (sr.getTotalItems() > 1) {

                System.out.println("Total items: " + sr.getTotalItems());
                }
        }catch(EVDBRuntimeException var){
                // catch block...
                System.out.println("Opps got runtime an error...");


        } catch( EVDBAPIException var){
                // catch block...
                System.out.println("Opps got runtime an error...");

        }
}

}
