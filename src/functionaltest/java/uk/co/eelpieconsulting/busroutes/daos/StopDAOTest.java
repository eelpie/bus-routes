package uk.co.eelpieconsulting.busroutes.daos;

import java.net.UnknownHostException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoException;

import uk.co.eelpieconsulting.busroutes.model.Stop;

public class StopDAOTest {
	
	private static final int YORK_ROAD_STOP = 53550;

	private StopDAO stopDAO;
	
	@Before
	public void setup() throws UnknownHostException, MongoException {
		DataSourceFactory dataStoreFactory = new DataSourceFactory("127.0.0.1", "buses");
		stopDAO = new StopDAO(dataStoreFactory);
	}

	@Test
	public void canFindNearbyStops() throws Exception {
		final Stop stop = stopDAO.getStop(YORK_ROAD_STOP);
		
		final List<Stop> stopsNear = stopDAO.findStopsNear(stop.getLatitude(), stop.getLongitude());
		
		for (Stop nearbyStop : stopsNear) {
			System.out.println(nearbyStop);
		}
		// TODO asserts
	}
	
}
