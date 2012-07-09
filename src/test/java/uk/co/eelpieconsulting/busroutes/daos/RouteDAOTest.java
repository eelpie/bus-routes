package uk.co.eelpieconsulting.busroutes.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.model.RouteStop;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;

public class RouteDAOTest {

	private static Datastore datastore;

	@BeforeClass
	public static void setup() throws Exception {
		Morphia morphia = new Morphia();
		Mongo m = new Mongo("dev.local");
		datastore = morphia.createDatastore(m, "buses");
	}
		
	@Test
	public void canLoadRoutesForGiveStop() throws Exception {
        final Query<RouteStop> q = datastore.createQuery(RouteStop.class).
        	filter("Bus_Stop_Code", 53550);        

        final Set<String> routeNames = new HashSet<String>();
        for (RouteStop routeStop : q.asList()) {
			routeNames.add(routeStop.getRoute());
		}
        System.out.println(routeNames);
        
        assertEquals(8, routeNames.size());        
        assertTrue(routeNames.contains("H22"));
        assertTrue(routeNames.contains("33"));
        assertTrue(routeNames.contains("R68"));
        assertTrue(routeNames.contains("N22"));
	}
	
	@Test
	public void canLoadStopsAlongGivenRoute() throws Exception {
        final Query<RouteStop> q = datastore.createQuery(RouteStop.class).
        	filter("Route", "H22").
        	filter("Run", 1);
        
        assertEquals(39, q.asList().size());
	}
	
}
