package uk.co.eelpieconsulting.busroutes.parsing;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.daos.RouteDAO;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class RouteImportServiceTest {

	private static Datastore datastore;
	
	private RouteImportService routeImportService;

	@BeforeClass
	public static void setupClass() throws Exception {
		Morphia morphia = new Morphia();
		Mongo m = new Mongo("dev.local");
		datastore = morphia.createDatastore(m, "buses");
	}

	@Before
	public void setup() {
		routeImportService = new RouteImportService(new RoutesParser(), new RouteDAO(datastore));		
	}
	
	@Test
	public void importRoutes() throws Exception {
		routeImportService.importRoutes();
	}
		
}
