package uk.co.eelpieconsulting.busroutes.daos;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.PersistedStop;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;
import uk.co.eelpieconsulting.busroutes.model.Stop;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.mongodb.MongoException;

@Component
public class RouteStopDAO {

	private static final String SEQUENCE = "sequence";
	private static final double NEAR_RADIUS = 0.01;
	private static final String RUN = "run";
	private static final String ROUTE = "route";
	private static final String STOP_ID = "stopId";
			
	private final Datastore datastore;

	@Autowired
	public RouteStopDAO(DataSourceFactory dataSourceFactory) throws UnknownHostException, MongoException {
		this.datastore = dataSourceFactory.getDatastore();
	}

	public List<RouteStop> findByStopId(int stopId) {
		final Query<RouteStop> q = datastore.createQuery(RouteStop.class).
			filter(STOP_ID, stopId);
		return q.asList();
	}
	
	public Stop getStop(int stopId) {
		final Query<PersistedStop> q = datastore.createQuery(PersistedStop.class).
			filter("id", stopId);
		return q.get();
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
	
	public RouteStop findLastForRoute(String route, int run) {
		final Query<RouteStop> q = datastore.createQuery(RouteStop.class).
   			filter(ROUTE, route).
   			filter(RUN, run).
   			order("-" + SEQUENCE);
		return q.get();		
	}

	public void addRouteStop(RouteStop routeStop) {
		datastore.save(routeStop);
	}

	public List<RouteStop> findNear(double latitude, double longitude) {
		final Query<RouteStop> query = datastore.find(RouteStop.class).
			field("location").within(latitude, longitude, NEAR_RADIUS);
		return query.asList();
	}

	public void removeAll() {
		final Query<RouteStop> allRouteStops = datastore.createQuery(RouteStop.class);
		datastore.delete(allRouteStops);
	}

	public void saveStop(PersistedStop stop) {
		datastore.save(stop);		
	}

	public void removeAllStops() {
		final Query<PersistedStop> allStops = datastore.createQuery(PersistedStop.class);
		datastore.delete(allStops);
	}

	public List<Stop> findStopsNear(double latitude, double longitude) {
		final Query<PersistedStop> query = datastore.createQuery(PersistedStop.class).
			field("location").within(latitude, longitude, NEAR_RADIUS);
		
		List<Stop> stops = new ArrayList<Stop>();
		for (PersistedStop persistedStop : query.asList()) {
			stops.add(persistedStop);
		}
		return stops;
	}
	
}
