package uk.co.eelpieconsulting.busroutes.model;

import java.util.ArrayList;
import java.util.List;

public class MultiStopMessage extends Message {
	
	private static final long serialVersionUID = 1L;
	
	private List<Stop> stops;
	
	public MultiStopMessage(String id, Message message) {
		super(id, message.getStopId(), message.getMessage(), message.getPriority(), message.getStartDate(), message.getEndDate());
		this.stops = new ArrayList<Stop>();
	}
	
	public List<Stop> getStops() {
		return stops;
	}
	
	public void addStop(Stop stop) {
		stops.add(stop);
	}

}
