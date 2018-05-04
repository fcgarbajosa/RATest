package service;

import java.util.List;

public class FlightsByMonth {
	
    private Integer month;
	private List<FlightsByDay> days;
	
	public FlightsByMonth(){
		
		// Empty to do the client call
	}
	
    public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}

    public List<FlightsByDay> getDays() {
		return days;
		}
	public void setDays(List<FlightsByDay> days) {
		this.days = days;
	}
	
}
