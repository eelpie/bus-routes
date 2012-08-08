package uk.co.eelpieconsulting.busroutes.model;

import java.util.Arrays;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

@Entity("routes")
public class RouteStop {

	@Id
	ObjectId objectId;

	@Indexed
	private int stopId;
		
	@Indexed
	private int run;
	
	@Indexed
	private int sequence;
	
	@Indexed
	private String route;
	
	private String stopName;
	private boolean virtualBusStop, nationalRail, tube, tram;
	
	private double[] location;
		
	public RouteStop() {
	}
	
	public RouteStop(int stopId, int run, boolean virtualBusStop, int sequence, String route, String stopName, double[] location, boolean nationalRail, boolean tube, boolean tram) {
		this.stopId = stopId;
		this.run = run;
		this.virtualBusStop = virtualBusStop;
		this.sequence = sequence;
		this.route = route;
		this.stopName = stopName;
		this.location = location;
		this.nationalRail = nationalRail;
		this.tube = tube;
		this.tram = tram;
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
	public double getLatitude() {
		return location[0];
	}
	public double getLongitude() {
		return location[1];
	}

	public boolean isNationalRail() {
		return nationalRail;
	}

	public boolean isTube() {
		return tube;
	}
	
	public Boolean isTram() {
		return tram;
	}

	@Override
	public String toString() {
		return "RouteStop [stopId=" + stopId + ", run=" + run + ", sequence="
				+ sequence + ", route=" + route + ", stopName=" + stopName
				+ ", virtualBusStop=" + virtualBusStop + ", nationalRail="
				+ nationalRail + ", tube=" + tube + ", tram=" + tram
				+ ", location=" + Arrays.toString(location) + "]";
	}
	
}
