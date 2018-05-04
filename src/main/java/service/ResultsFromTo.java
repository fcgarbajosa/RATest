package service;


import java.util.List;


public class ResultsFromTo {
	private  Integer stops;
	private List<FlightsFromTo> legs;
	  
	public Integer getStops() {
		return stops;
	}
	public void setStops(Integer stops) {
		this.stops = stops;
	}
	public List<FlightsFromTo> getLegs() {
		return legs;
	}
	public void setLegs(List<FlightsFromTo> legs) {
		this.legs = legs;
	}
	 
}
