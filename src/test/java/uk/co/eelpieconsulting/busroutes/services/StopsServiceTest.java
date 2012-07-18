package uk.co.eelpieconsulting.busroutes.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.daos.RouteStopDAO;
import uk.co.eelpieconsulting.busroutes.model.Route;
import uk.co.eelpieconsulting.busroutes.model.Stop;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class StopsServiceTest {

	private static final double LOCAL_LATITUDE = 51.4470;
	private static final double LOCAL_LONGITUDE = -0.3255;
	private static final String EXPECTED_LOCAL_STOP = "TWICKENHAM STATION";

	private static Datastore datastore;	
	
	private StopsService stopsService;
	
	@BeforeClass
	public static void setupClass() throws Exception {
		Morphia morphia = new Morphia();
		Mongo m = new Mongo("dev.local");
		datastore = morphia.createDatastore(m, "buses");
	}
	
	@Before
	public void setup() {
		RouteStopDAO routeDAO = new RouteStopDAO(datastore);
		stopsService = new StopsService(routeDAO);
	}	
	
	@Test
	public void canFindStopsNearLocation() throws Exception {		
		Set<Stop> stopsNear = stopsService.findStopsNear(LOCAL_LATITUDE, LOCAL_LONGITUDE);

		for (Stop stop : stopsNear) {
			if (stop.getName().equals(EXPECTED_LOCAL_STOP)) {
				assertTrue(true);
				return;
			}
		}
		fail("Did not see expected near by stop");
	}
	
	@Test
	public void canRoutesNearLocation() throws Exception {		
		Set<Route> routesNear = stopsService.findRoutesNear(51.4470, LOCAL_LONGITUDE);
		
		assertTrue(routesNear.contains(new Route("H22", 1)));
		assertTrue(routesNear.contains(new Route("H22", 2)));
		assertFalse(routesNear.contains(new Route("63", 1)));
	}
	
}
