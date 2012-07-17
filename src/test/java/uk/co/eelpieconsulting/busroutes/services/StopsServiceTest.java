package uk.co.eelpieconsulting.busroutes.services;

import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.daos.RouteDAO;
import uk.co.eelpieconsulting.busroutes.model.Route;
import uk.co.eelpieconsulting.busroutes.model.Stop;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class StopsServiceTest {

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
		RouteDAO routeDAO = new RouteDAO(datastore);
		stopsService = new StopsService(routeDAO);
	}	
	
	@Test
	public void canFindStopsNearLocation() throws Exception {		
		Set<Stop> stopsNear = stopsService.findStopsNear(51.4470, -0.3255);
		System.out.println(stopsNear);		// TODO asserts	
	}
	
	@Test
	public void canRoutesNearLocation() throws Exception {		
		Set<Route> routesNear = stopsService.findRoutesNear(51.4470, -0.3255);
		System.out.println(routesNear);		// TODO asserts	
	}
	
}
