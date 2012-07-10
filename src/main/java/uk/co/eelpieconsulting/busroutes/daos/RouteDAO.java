package uk.co.eelpieconsulting.busroutes.daos;

import java.util.List;

import uk.co.eelpieconsulting.busroutes.model.RouteStop;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;

public class RouteDAO {

	private final Datastore datastore;

	public RouteDAO(Datastore datastore) {
		this.datastore = datastore;
	}

	public List<RouteStop> getRoutesForStop(int stopId) {
		final Query<RouteStop> q = datastore.createQuery(RouteStop.class)
				.filter("Bus_Stop_Code", stopId);
		return q.asList();
	}

	public List<RouteStop> getStopsForRoute(String routeName, int run) {
		final Query<RouteStop> q = datastore.createQuery(RouteStop.class).
       		filter("Route", routeName).
       		filter("Run", run);
		return q.asList();
	}

	public RouteStop getStopByIdentifier(String stopIdentifer) {
		final Query<RouteStop> q = datastore.createQuery(RouteStop.class).
			filter("Stop_Code_LBSL", stopIdentifer);
		return q.get();
	}
	
}
