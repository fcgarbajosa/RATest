package service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class RestClient {
	
	  private static final Logger log = LoggerFactory.getLogger(RestClient.class);
	  private static final Route[] emptyRoutes = {};

	   public Route[] findAllRoutes(){
		   
		   RestTemplate restTemplate = new RestTemplate();		   
		   
		   try {
	            ResponseEntity<Route[]> responseEntity = restTemplate.getForEntity("https://api.ryanair.com/core/3/routes", Route[].class);	      
	            return responseEntity.getBody();
	            
		   } catch (HttpClientErrorException ex){
			   String errorMessage = String.format("ERROR URL https://api.ryanair.com/core/3/routes :%s-%s", ex.getStatusCode(), ex.getMessage());
	        	log.error(errorMessage);
	       } catch (Exception ex){
	    	   String errorMessage = String.format("ERROR URL https://api.ryanair.com/core/3/routes : %s", ex.getMessage());
	    	   log.error(errorMessage);
	       }
	       
		   return emptyRoutes;
	  
	   }	   
	   
	   public List<FlightsByDay> findSchedules(String departureAirportFrom, String departureAirportTo, String month, String year){
		   
	        RestTemplate restTemplate = new RestTemplate();
	        FlightsByMonth flightsByMonth = null;
	        List<FlightsByDay> flightsByDay = new ArrayList<>();
				           
	        try{
	        	String restCallUrl = String.format("https://api.ryanair.com/timetable/3/schedules/%s/%s/years/%s/months/%s", departureAirportFrom, departureAirportTo, year, month);
		        ResponseEntity<FlightsByMonth> responseEntity = restTemplate.getForEntity(restCallUrl, FlightsByMonth.class);
		        flightsByMonth = responseEntity.getBody();
		        flightsByDay = flightsByMonth.getDays(); 
	        } catch (HttpClientErrorException ex){
	        	String errorMessage = String.format("ERROR, URL https://api.ryanair.com/timetable/3/schedules/%s/%s/years/%s/month/%s/ - Status Code %s :: %s", departureAirportFrom, departureAirportTo, year, month, ex.getStatusCode(), ex.getMessage());
	        	log.error(errorMessage);
	        } catch (Exception ex){
	        	String errorMessage = String.format("ERROR, URL https://api.ryanair.com/timetable/3/schedules/%s/%s/years/%s/months/%s/ ::%s", departureAirportFrom, departureAirportTo, year, month, ex.getMessage());
		    	log.error(errorMessage);
		    }
	        
	        return flightsByDay;

	   }  
}
