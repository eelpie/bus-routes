package uk.co.eelpieconsulting.busroutes.daos;

import java.util.List;

import uk.co.eelpieconsulting.busroutes.model.RouteStop;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;

public class RouteDAO {

	private static final String RUN = "run";
	private static final String ROUTE = "route";
	private static final String STOP_ID = "stopId";
	
	private final Datastore datastore;

	public RouteDAO(Datastore datastore) {
		this.datastore = datastore;
	}

	public List<RouteStop> getRoutesForStop(int stopId) {
		final Query<RouteStop> q = datastore.createQuery(RouteStop.class).
			filter(STOP_ID, stopId);
		return q.asList();
	}

	public List<RouteStop> getStopsForRoute(String routeName, int run) {
		final Query<RouteStop> q = datastore.createQuery(RouteStop.class).
       		filter(ROUTE, routeName).
       		filter(RUN, run);
		return q.asList();
	}

	public RouteStop getStopById(int stopId) {
		final Query<RouteStop> q = datastore.createQuery(RouteStop.class).
			field(STOP_ID).equal(stopId);
		return q.get();
	}
	
	public void addRouteStop(RouteStop routeStop) {
		datastore.save(routeStop);
	}

	public List<RouteStop> findStopsNear(double latitude, double longitude) {
		final Query<RouteStop> query = datastore.find(RouteStop.class).
			field("location").near(latitude, longitude);
		return query.asList();
	}
	
}
