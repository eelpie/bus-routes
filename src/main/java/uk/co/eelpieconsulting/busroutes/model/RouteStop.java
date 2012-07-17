package uk.co.eelpieconsulting.busroutes.model;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("routes")
public class RouteStop {

	@Id
	ObjectId objectId;

	private int stopId, run, sequence;
	private String route, stopName;
	private boolean virtualBusStop;

	public RouteStop(int stopId, int run, boolean virtualBusStop, int sequence, String route, String stopName) {
		this.stopId = stopId;
		this.run = run;
		this.virtualBusStop = virtualBusStop;
		this.sequence = sequence;
		this.route = route;
		this.stopName = stopName;
	}
	
	public ObjectId getObjectId() {
		return objectId;
	}
	public int getRun() {
		return run;
	}
	public int getBus_Stop_Code() {
		return stopId;
	}
	public String getRoute() {
		return route;
	}
	public String getStop_Name() {
		return stopName;
	}	
	public boolean getVirtual_Bus_Stop() {
		return virtualBusStop;
	}	
	public int getSequence() {
		return sequence;
	}

	@Override
	public String toString() {
		return "RouteStop [route=" + route + ", run=" + run + ", sequence="
				+ sequence + ", stopId=" + stopId + ", stopName=" + stopName
				+ ", virtualBusStop=" + virtualBusStop + "]";
	}
	
}
