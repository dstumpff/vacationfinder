package template.main;

import java.util.ArrayList;

class SearchCriteria{
	String price, vacationType, hotel, startDate, endDate;
	int travelDistance, numOfVacationers;
	SearchCriteria(){
		price = vacationType = hotel = "";
	    travelDistance = numOfVacationers = 0;
	    
	}
	
	SearchCriteria(String price, String vacationType, int travelDistance, int numOfVacationers, String hotel){
		this.price = price;
		this.vacationType = vacationType;
		this.travelDistance = travelDistance;
		this.numOfVacationers = numOfVacationers;
		this.hotel = hotel;
	}
}

public class Facade {
	
	/** The static instance of the Facade. **/
	private static Facade theInstance = new Facade();
	/** The static ArrayList for Google Data. Accessed by the getGoogleData() method.**/
	private SearchCriteria searchCriteria = new SearchCriteria();
	
	/**
	 * Tests the data using hard coded values.
	 * @param args
	 */
	public static void main(String[] args){
		
	}
	
	/**
	 * Returns the instance of the Facade.
	 * 
	 * @return the Facade instance
	 */
	public static Facade getInstance(){
		if(theInstance == null){
			theInstance = new Facade();
		}
		
		return theInstance;
	}
	
	public SearchCriteria getSearchCriteria(){
		return searchCriteria;
	}
}
