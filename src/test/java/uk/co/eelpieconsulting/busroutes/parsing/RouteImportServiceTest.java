package uk.co.eelpieconsulting.busroutes.parsing;

import java.io.InputStream;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.daos.DataSourceFactory;
import uk.co.eelpieconsulting.busroutes.daos.RouteStopDAO;
import uk.co.eelpieconsulting.busroutes.daos.StopDAO;
import uk.co.eelpieconsulting.busroutes.geo.OSRefConvertor;
import uk.co.eelpieconsulting.busroutes.services.StopsService;

import com.mongodb.MongoException;

public class RouteImportServiceTest {
	
	private RouteImportService routeImportService;

	@Before
	public void setup() throws UnknownHostException, MongoException {
		DataSourceFactory dataStoreFactory = new DataSourceFactory("127.0.0.1", "buses");
		RouteStopDAO routeDAO = new RouteStopDAO(dataStoreFactory);
		StopDAO stopDAO = new StopDAO(dataStoreFactory);
		routeImportService = new RouteImportService(new RoutesParser(new RouteLineParser(new OSRefConvertor())), routeDAO, stopDAO, new StopsService(routeDAO, stopDAO));		
	}
	
	@Test
	public void importRoutes() throws Exception {
		final InputStream input = this.getClass().getClassLoader().getResourceAsStream("routes.csv");
		routeImportService.importRoutes(input);
	}
	
}