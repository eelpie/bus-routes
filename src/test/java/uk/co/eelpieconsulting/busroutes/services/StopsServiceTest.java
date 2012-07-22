package uk.co.eelpieconsulting.busroutes.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.daos.DataSourceFactory;
import uk.co.eelpieconsulting.busroutes.daos.RouteStopDAO;
import uk.co.eelpieconsulting.busroutes.daos.StopDAO;
import uk.co.eelpieconsulting.busroutes.model.Route;
import uk.co.eelpieconsulting.busroutes.model.Stop;

import com.mongodb.MongoException;

public class StopsServiceTest {

	private static final double LOCAL_LATITUDE = 51.4470;
	private static final double LOCAL_LONGITUDE = -0.3255;
	private static final String EXPECTED_LOCAL_STOP = "TWICKENHAM STATION";
	
	private StopsService stopsService;
	
	@Before
	public void setup() throws UnknownHostException, MongoException {
		DataSourceFactory dataStoreFactory = new DataSourceFactory("dev.local", "buses");
		stopsService = new StopsService(new RouteStopDAO(dataStoreFactory), new StopDAO(dataStoreFactory));
	}	
	
	@Test
	public void canFindStopsNearLocation() throws Exception {		
		List<Stop> stopsNear = stopsService.findStopsNear(LOCAL_LATITUDE, LOCAL_LONGITUDE);

		for (Stop stop : stopsNear) {
			if (stop.getName().equals(EXPECTED_LOCAL_STOP)) {
				assertTrue(true);
				return;
			}
		}
		fail("Did not see expected nearby stop");
	}
	
	@Test
	public void canFindRoutesNearLocation() throws Exception {		
		final Set<Route> routesNear = stopsService.findRoutesNear(51.4470, LOCAL_LONGITUDE);

		assertTrue(routesNear.contains(new Route("H22", 1, null)));
		assertTrue(routesNear.contains(new Route("H22", 2, null)));
		assertFalse(routesNear.contains(new Route("63", 1, null)));
	}
	
	@Test
	public void canFindAllStopsForRoute() throws Exception {
		final List<Stop> stops = stopsService.findStopsForRoute("R68", 2);
		
		assertEquals(47, stops.size());
		assertEquals("HAMPTON COURT STATION", stops.get(0).getName());
		assertEquals("KEW RETAIL PARK", stops.get(46).getName());
	}
	
}
