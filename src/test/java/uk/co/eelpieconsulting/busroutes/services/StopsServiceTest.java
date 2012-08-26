package uk.co.eelpieconsulting.busroutes.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
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
	private static final String EXPECTED_LOCAL_STOP = "Twickenham Station";
	
	private StopsService stopsService;
	
	@Before
	public void setup() throws UnknownHostException, MongoException, MalformedURLException {
		DataSourceFactory dataStoreFactory = new DataSourceFactory("127.0.0.1", "buses");
		SolrServer solrServer = new CommonsHttpSolrServer("http://127.0.0.1:8080/apache-solr-3.6.1/buses");
		stopsService = new StopsService(new RouteStopDAO(dataStoreFactory), new StopDAO(dataStoreFactory), solrServer);
	}	
	
	@Test
	public void canFindStopsNearLocation() throws Exception {		
		List<Stop> stopsNear = stopsService.findStopsNear(LOCAL_LATITUDE, LOCAL_LONGITUDE);

		for (Stop stop : stopsNear) {
			System.out.println(stop);
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
		assertEquals("Hampton Court Station", stops.get(0).getName());
		assertEquals("Kew Retail Park", stops.get(46).getName());
	}
	
	@Test
	public void canFindStopByPartialName() throws Exception {
		List<Stop> searchResults = stopsService.search("twickenham");		
		for (Stop stop : searchResults) {
			System.out.println(stop);
			if (stop.getId() == 53978) {
				return;
			}
		}
		fail("Did not see expected stop; Twickenham Station");
	}
}
