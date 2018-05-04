package service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import business.FlightGetter;

/**
 * Rest web Controller
 * 
 * Defined by the specifications. ´
 * 
 * @author Fidel C. Garbajosa
 * 
 * 2018/05/04
 * 
 */

@RestController
public class ResultsFromToController {
	
	private static final Logger log = LoggerFactory.getLogger(RestClient.class);
	  
    @RequestMapping("/interconnections")
    public ResultsFromTo[] interconnections(@RequestParam(value="departure", defaultValue="") String departure, 
    		                        @RequestParam(value="arrival", defaultValue="") String arrival,
    		                        @RequestParam(value="departureDateTime", defaultValue="") String departureDateTime,
    		                        @RequestParam(value="arrivalDateTime", defaultValue="") String arrivalDateTime) {
    	
    	log.info("begin rest service");
    	
    	log.info("Getting routes");
    	RestClient restClient = new RestClient();
    	Route[] routes = restClient.findAllRoutes();
        log.info("End getting routes");
        
        
        FlightGetter flightGetter = new FlightGetter();
        ResultsFromTo[] rft = new ResultsFromTo[2];
       
    	// Get Direct Flights (No scales)        
        log.info("begin getDirectFlights");    	        
        rft[0] = flightGetter.getDirectFlights(departure, arrival, departureDateTime, arrivalDateTime, routes);
        log.info("end getDirectFlights");
        
    	// Get One Stop Flights (1 scale) 
        log.info("begin getOneStopFlights");        
        rft[1] = flightGetter.getOneStopFlights(departure, arrival, departureDateTime, arrivalDateTime, routes);
        log.info("end getOneStopFlights");
        
        log.info("found flights:");
        if (!rft[0].getLegs().isEmpty()){
            List<FlightsFromTo> flightsDirect = rft[0].getLegs();
            for (FlightsFromTo f: flightsDirect){
                log.info("found direct flight:");
                log.info("{} {} {} {}", departure, f.getDepartureDateTime(), arrival,  f.getArrivalDateTime());	
            }
        }        
        if (!rft[1].getLegs().isEmpty()){
        	List<FlightsFromTo> flightsOneStop = rft[1].getLegs();        	 
        	Integer i=0;
            for (FlightsFromTo f: flightsOneStop){            	
            	if (i % 2 == 0)
            		log.info("found 1 stop flight:");            
                log.info(" {} {} {} {}", f.getDepartureAirport(), f.getDepartureDateTime() , f.getArrivalAirport(), f.getArrivalDateTime());	
                i++;
            }
        }
        
        log.info("end rest service");
        
    	return rft;
    	
    }
}
