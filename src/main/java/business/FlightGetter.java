package business;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import service.Flight;
import service.FlightsByDay;
import service.FlightsFromTo;
import service.RestClient;
import service.ResultsFromTo;
import service.Route;

/**
 * 
 * 
 * Class to get the direct flights and 1 stop flights ´
 * 
 * @author Fidel C. Garbajosa
 * 
 * 2018/05/04
 * 
 */

public class FlightGetter {

    public Boolean findDirectRoute(String departure, String arrival, Route[] routes){
		   
	    for (int i=0; i<routes.length; i++){
	      	if (routes[i].getConnectingAirport() == null &&
	           Objects.equals(routes[i].getAirportFrom(), departure) == Boolean.TRUE && 
	           Objects.equals(routes[i].getAirportTo(), arrival) == Boolean.TRUE){
	           	return Boolean.TRUE;		
	        }	            		    
	    }	       
	       
		return Boolean.FALSE;
	  
	}
	
	public ResultsFromTo getDirectFlights(String departure, String arrival, String departureDateTime, String arrivalDateTime, Route[] routes){
		
	  	RestClient rc = new RestClient();

    	ResultsFromTo resultsDirectFligh = new ResultsFromTo();    	
    	resultsDirectFligh.setStops(0);
		
		List<FlightsFromTo> legs = new ArrayList<>();    
		
    	if (findDirectRoute(departure, arrival, routes)){
    		
    	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    	    LocalDateTime departureDateTimeOk = LocalDateTime.parse(departureDateTime, formatter);
    	    LocalDateTime arrivalDateTimeOk = LocalDateTime.parse(arrivalDateTime, formatter);
            
    	    for (int year = departureDateTimeOk.getYear(); year <= arrivalDateTimeOk.getYear(); year++){
                for (int month = departureDateTimeOk.getMonthValue(); month <= 12 && month <= arrivalDateTimeOk.getMonthValue(); month++){
                	
                	List<FlightsByDay> flightsByDay = rc.findSchedules(departure, arrival, Integer.toString(month), Integer.toString(year));
                	if (!flightsByDay.isEmpty()){
                                  	   
                	   legs.addAll(getDirectFightsMonthDay(flightsByDay, year, month, departure, arrival, departureDateTime, arrivalDateTime));    
                	}
                }
    	    }

    	}
		resultsDirectFligh.setLegs(legs);
		return resultsDirectFligh;
	}
	
	private List<FlightsFromTo> getDirectFightsMonthDay(List<FlightsByDay> flightsByDay, Integer year, Integer month, String departure, String arrival, String departureDateTime, String arrivalDateTime){
		FlightsFromTo f1 = new FlightsFromTo();
		List<FlightsFromTo> legs = new ArrayList<>();
        for (FlightsByDay fbd: flightsByDay){
 	        List<Flight> fl = fbd.getFlights();
 	        for (Flight f: fl){
 		    
 		        String monthOk;
 	 	        if (month <10) monthOk = "0" + month;
 		        else monthOk = "" + month;
 		   
 		        String dayOk;                    		   
 		        if (fbd.getDay()<10) dayOk = "0" + fbd.getDay();
 		        else dayOk = "" + fbd.getDay();
 		   
 		        String flightDepartureTime = year + "-" + monthOk +"-" + dayOk+"T"+f.getDepartureTime();
 		        String flightArrivalTime = year + "-" + monthOk +"-" + dayOk+"T"+f.getArrivalTime();
 		   
 		        if (flightDepartureTime.compareTo(departureDateTime) >= 0 && 
 				    flightArrivalTime.compareTo(arrivalDateTime) <0){
 			   
 		           f1.setDepartureAirport(departure);
 		           f1.setArrivalAirport(arrival);
 		           f1.setDepartureDateTime(flightDepartureTime);
 		           f1.setArrivalDateTime(flightArrivalTime); 
 				   legs.add(f1);
 		        
 		        }	                       		   
 	        }
        }
        return legs;
	}
	
	public ResultsFromTo getOneStopFlights(String departure, String arrival, String departureDateTime, String arrivalDateTime, Route[] routes){
			  	
    	ResultsFromTo resultsOneStop = new ResultsFromTo();    	
		resultsOneStop.setStops(1);
				
	  	List<Route> routeBeginDeparture = new ArrayList<>();
	  	List<Route> routeEndArrival = new ArrayList<>();
	  	
	  	getRoutesBeginDepartureEndArrival(routeBeginDeparture, routeEndArrival, departure, arrival, routes);
	  	
		List<FlightsFromTo> legs = new ArrayList<>();
		
	  	for (Route rbd : routeBeginDeparture){
		  	for (Route rea : routeEndArrival){ 
		  		
		  		if (Objects.equals(rbd.getAirportTo(), rea.getAirportFrom())){
		  		   	
		  		   ResultsFromTo directFlightsFirst = getDirectFlights(departure, rbd.getAirportTo(), departureDateTime, arrivalDateTime, routes);
			  	   List<FlightsFromTo> flightsFirst = directFlightsFirst.getLegs();
			  	   
		  		   if (!flightsFirst.isEmpty()){
			  		   Route[] rBdRa = new Route[2];
			  		   rBdRa[0] = rbd;
			  		   rBdRa[1] = rea;
			  		   legs.addAll(getSecondFlights(flightsFirst, rBdRa, departure, arrival, departureDateTime, arrivalDateTime, routes));
		  		   }  
		  		}
	  	    }
	  	}        
	  	resultsOneStop.setLegs(legs);
	  	
		return resultsOneStop;
	}
	
	private void getRoutesBeginDepartureEndArrival(List<Route> routeBeginDeparture, List<Route> routeEndArrival, String departure, String arrival, Route[] routes) {
		
	  	for (int i=0; i<routes.length; i++){
	  		
	  		if (routes[i].getConnectingAirport() == null && Objects.equals(routes[i].getAirportFrom(), departure))
	  		   routeBeginDeparture.add(routes[i]);
	  		if (routes[i].getConnectingAirport() == null && Objects.equals(routes[i].getAirportTo(), arrival))
	  			routeEndArrival.add(routes[i]);
	  	}
		
	}
	
	private List<FlightsFromTo> getSecondFlights(List<FlightsFromTo> flightsFirst, Route[] rBdRa, String departure, String arrival, String departureDateTime, String arrivalDateTime, Route[] routes){
		
	   Route rbd = rBdRa[0];
	   Route rea = rBdRa[1];
		
	   List<FlightsFromTo> legs = new ArrayList<>();	
       for (FlightsFromTo fft: flightsFirst){
  			  
		   ResultsFromTo directFlightsSecond = getDirectFlights(rbd.getAirportTo(), arrival, departureDateTime, arrivalDateTime, routes);
  		   List<FlightsFromTo> flightsSecond = directFlightsSecond.getLegs();
  			  
  		   if (!flightsSecond.isEmpty()){
		
              for (FlightsFromTo ffts: flightsSecond){			  			    	  
	              if (ffts.getDepartureAirport() != null) {
	    		 
	     	         // Check date restrictions on the second 
	    	         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	    	         LocalDateTime departureDateTimeOk = LocalDateTime.parse(fft.getArrivalDateTime(), formatter).plusHours(2L);			  			    	     

	    	         if (ffts.getDepartureDateTime().compareTo(departureDateTimeOk.toString()) > 0){
		            
		      	        FlightsFromTo flightFirst = new FlightsFromTo();
		      	        flightFirst.setDepartureAirport(departure);
		      	        flightFirst.setArrivalAirport(rbd.getAirportTo());
		      	        flightFirst.setDepartureDateTime(fft.getDepartureDateTime());
		      	        flightFirst.setArrivalDateTime(fft.getArrivalDateTime());
		      	    
		      	        FlightsFromTo flightSecond = new FlightsFromTo();
		      	        flightSecond.setDepartureAirport(rea.getAirportFrom());
		      	        flightSecond.setArrivalAirport(rea.getAirportTo());
		      	        flightSecond.setDepartureDateTime(ffts.getDepartureDateTime());
		      	        flightSecond.setArrivalDateTime(ffts.getArrivalDateTime());
		      	    
		  		        legs.add(flightFirst);
		  		        legs.add(flightSecond);
		  		    
	    	         }
	              }
	          }
  		   } 
        }  
        return legs;
	}
}
