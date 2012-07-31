package uk.co.eelpieconsulting.busroutes.daos;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.PersistedStop;
import uk.co.eelpieconsulting.busroutes.model.Stop;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.mongodb.MongoException;

@Component
public class StopDAO {

	private static final String ID = "id";
	private static final String LOCATION = "location";
	private static final double NEAR_RADIUS = 0.01;
			
	private final Datastore datastore;

	@Autowired
	public StopDAO(DataSourceFactory dataSourceFactory) throws UnknownHostException, MongoException {
		this.datastore = dataSourceFactory.getDatastore();
	}
	
	public PersistedStop getStop(int stopId) {
		final Query<PersistedStop> q = datastore.createQuery(PersistedStop.class).
			filter(ID, stopId);
		return q.get();
	}
	
	public List<Stop> findStopsNear(double latitude, double longitude) {
		final Query<PersistedStop> query = datastore.createQuery(PersistedStop.class).
			field(LOCATION).within(latitude, longitude, NEAR_RADIUS);
		
		List<Stop> stops = new ArrayList<Stop>();
		for (PersistedStop persistedStop : query.asList()) {
			stops.add(persistedStop);
		}
		return stops;
	}
	
	public List<PersistedStop> getAll() {
		final Query<PersistedStop> query = datastore.createQuery(PersistedStop.class);
		return query.asList();
	}
	
	public void saveStop(PersistedStop stop) {
		datastore.save(stop);		
	}

	public void removeAllStops() {
		final Query<PersistedStop> allStops = datastore.createQuery(PersistedStop.class);
		datastore.delete(allStops);
	}
	
}
