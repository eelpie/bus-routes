package uk.co.eelpieconsulting.busroutes.model;

import java.util.Arrays;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;

@Entity("routes")
public class RouteStop {

	@Id
	ObjectId objectId;

	private int stopId, run, sequence;
	private String route, stopName;
	private boolean virtualBusStop;
	@Indexed(IndexDirection.GEO2D)
	private double[] location;
		
	public RouteStop() {
	}
	
	public RouteStop(int stopId, int run, boolean virtualBusStop, int sequence, String route, String stopName, double[] location) {
		this.stopId = stopId;
		this.run = run;
		this.virtualBusStop = virtualBusStop;
		this.sequence = sequence;
		this.route = route;
		this.stopName = stopName;
		this.location = location;
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
	public double[] getLocation() {
		return location;
	}

	@Override
	public String toString() {
		return "RouteStop [location=" + Arrays.toString(location) + ", route="
				+ route + ", run=" + run + ", sequence=" + sequence
				+ ", stopId=" + stopId + ", stopName=" + stopName
				+ ", virtualBusStop=" + virtualBusStop + "]";
	}
	
}
