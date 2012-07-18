package uk.co.eelpieconsulting.busroutes.daos;

import java.util.List;

import uk.co.eelpieconsulting.busroutes.model.RouteStop;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;

public class RouteStopDAO {

	private static final String SEQUENCE = "sequence";
	private static final double NEAR_RADIUS = 0.1;
	private static final String RUN = "run";
	private static final String ROUTE = "route";
	private static final String STOP_ID = "stopId";
	
	private final Datastore datastore;

	public RouteStopDAO(Datastore datastore) {
		this.datastore = datastore;
	}

	public List<RouteStop> findByStopId(int stopId) {
		final Query<RouteStop> q = datastore.createQuery(RouteStop.class).
			filter(STOP_ID, stopId);
		return q.asList();
	}

	public RouteStop getFirstForStopId(int stopId) {
		final Query<RouteStop> q = datastore.createQuery(RouteStop.class).
		field(STOP_ID).equal(stopId);
		return q.get();
	}
	
	public List<RouteStop> findByRoute(String routeName, int run) {
		final Query<RouteStop> q = datastore.createQuery(RouteStop.class).
       		filter(ROUTE, routeName).
       		filter(RUN, run).
       		order(SEQUENCE);		
       	return q.asList();
	}

	public void addRouteStop(RouteStop routeStop) {
		datastore.save(routeStop);
	}

	public List<RouteStop> findNear(double latitude, double longitude) {
		final Query<RouteStop> query = datastore.find(RouteStop.class).
			field("location").within(latitude, longitude, NEAR_RADIUS);
		return query.asList();
	}
	
}
