package uk.co.eelpieconsulting.busroutes.model;

import java.util.Set;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.utils.IndexDirection;

@Entity("stops")
public class PersistedStop extends Stop {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	@Id
	private int id;
	
	@Indexed(IndexDirection.GEO2D)
	private double[] location;
	
	@SuppressWarnings("unused")
	@Embedded
	private Set<Route> routes;
	
	public PersistedStop() {
		super();
	}

	public PersistedStop(Stop stop) {
		super(stop.getId(), stop.getName(), stop.getTowards(), stop.getIndicator(), stop.getLatitude(), stop.getLongitude(), stop.isNationalRail(), stop.isTube(), stop.isTram());
		this.id = stop.getId();
		this.location = new double[2];
		this.location[0] = stop.getLatitude();
		this.location[1] = stop.getLongitude();
		this.routes = stop.getRoutes();
	}

}
