package uk.co.eelpieconsulting.busroutes.daos;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.RouteStop;

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

	public void removeAll() {
		final Query<RouteStop> allRecords = datastore.createQuery(RouteStop.class);
		datastore.delete(allRecords);
	}
	
}
