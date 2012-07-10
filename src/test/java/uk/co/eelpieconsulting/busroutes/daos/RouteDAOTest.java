package uk.co.eelpieconsulting.busroutes.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.model.RouteStop;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class RouteDAOTest {

	private static Datastore datastore;
	
	private RouteDAO routeDAO;

	@BeforeClass
	public static void setupClass() throws Exception {
		Morphia morphia = new Morphia();
		Mongo m = new Mongo("dev.local");
		datastore = morphia.createDatastore(m, "buses");
	}

	@Before
	public void setup() {
		routeDAO = new RouteDAO(datastore);
	}
	
	@Test
	public void canIdentifyStopFromPublicStopIdentifier() throws Exception {		
		final RouteStop stop = routeDAO.getStopByIdentifier("29987");

		assertEquals("YORK STREET / TWICKENHAM", stop.getStop_Name());
	}
	
	@Test
	public void canIdentifyStopFromPublicStopIdentifierEvenIfIdentiferContainsLetters() throws Exception {		
		final RouteStop stop = routeDAO.getStopByIdentifier("BP390");
		
		assertEquals("BERBERIS WALK", stop.getStop_Name());
	}
	
	@Test
	public void canLoadRoutesForGiveStop() throws Exception {
        final Set<String> routeNames = new HashSet<String>();
        
        for (RouteStop routeStop : routeDAO.getRoutesForStop(53550)) {
			routeNames.add(routeStop.getRoute());
		}        
        assertEquals(8, routeNames.size());        
        assertTrue(routeNames.contains("H22"));
        assertTrue(routeNames.contains("33"));
        assertTrue(routeNames.contains("R68"));
        assertTrue(routeNames.contains("N22"));
	}
	
	@Test
	public void canLoadStopsAlongGivenRoute() throws Exception {        
        final List<RouteStop> stops = routeDAO.getStopsForRoute("H22", 1);

        assertEquals(39, stops.size()); 
        assertEquals("THE BELL", stops.get(0).getStop_Name());
        assertEquals("MANOR ROAD", stops.get(stops.size()-1).getStop_Name());
	}
	
}
