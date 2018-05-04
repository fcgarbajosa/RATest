package service;

import java.util.List;

public class FlightsByDay {
	
     private Integer day;
     private List<Flight> flights;
     
     public FlightsByDay(){
    	 
    	// Empty to do the client call
     }
     
     public Integer getDay() {
	  	return day;
	 }
	 public void setDay(Integer day) {
	 	this.day = day;
	 }

	 public List<Flight> getFlights() {
	 	return flights;
	 }
	 public void setFlights(List<Flight> flights) {
	 	this.flights = flights;
	 }
}
