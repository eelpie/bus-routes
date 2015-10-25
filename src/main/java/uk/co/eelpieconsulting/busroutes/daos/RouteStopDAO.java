package uk.co.eelpieconsulting.busroutes.daos;

import java.net.UnknownHostException;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.RouteStop;

import com.mongodb.MongoException;

@Component
public class RouteStopDAO {

	private static final String SEQUENCE = "sequence";
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

	public void removeAll() {
		final Query<RouteStop> allRouteStops = datastore.createQuery(RouteStop.class);
		datastore.delete(allRouteStops);
	}
	
}
