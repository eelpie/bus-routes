package uk.co.eelpieconsulting.busroutes.parsing;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.daos.DataSourceFactory;
import uk.co.eelpieconsulting.busroutes.daos.RouteStopDAO;
import uk.co.eelpieconsulting.busroutes.geo.OSRefConvertor;
import uk.co.eelpieconsulting.busroutes.services.StopsService;

import com.mongodb.MongoException;

public class RouteImportServiceTest {
	
	private RouteImportService routeImportService;

	@Before
	public void setup() throws UnknownHostException, MongoException {
		DataSourceFactory dataStoreFactory = new DataSourceFactory("dev.local", "buses");
		RouteStopDAO routeDAO = new RouteStopDAO(dataStoreFactory);
		routeImportService = new RouteImportService(new RoutesParser(new RouteLineParser(new OSRefConvertor())), routeDAO, new StopsService(routeDAO));		
	}
	
	@Test
	public void importRoutes() throws Exception {
		routeImportService.importRoutes();
	}
		
}
