package uk.co.eelpieconsulting.busroutes.model;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("routes")
public class RouteStop {

	@Id
	ObjectId objectId;

	private int Bus_Stop_Code, Run;
	private String Route, Stop_Name, Stop_Code_LBSL;
	
	public ObjectId getObjectId() {
		return objectId;
	}
	public int getRun() {
		return Run;
	}
	public int getBus_Stop_Code() {
		return Bus_Stop_Code;
	}
	public String getRoute() {
		return Route;
	}
	public String getStop_Name() {
		return Stop_Name;
	}
	public String getStop_Code_LBSL() {
		return Stop_Code_LBSL;
	}
	
}
