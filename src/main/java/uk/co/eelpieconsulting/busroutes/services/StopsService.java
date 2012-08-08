package uk.co.eelpieconsulting.busroutes.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.daos.RouteStopDAO;
import uk.co.eelpieconsulting.busroutes.daos.StopDAO;
import uk.co.eelpieconsulting.busroutes.model.Route;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;
import uk.co.eelpieconsulting.busroutes.model.Stop;
import uk.co.eelpieconsulting.busroutes.parsing.RouteImportService;

@Component
public class StopsService {

	private static Logger log = Logger.getLogger(RouteImportService.class);

	private RouteStopDAO routeStopDAO;
	private StopDAO stopDAO;
	
	public StopsService() {
	}

	@Autowired
	public StopsService(RouteStopDAO routeDAO, StopDAO stopDAO) {
		this.routeStopDAO = routeDAO;
		this.stopDAO = stopDAO;
	}
	
	public Stop getStopById(int id) {
		return stopDAO.getStop(id);
	}
	
	public List<Stop> search(String q) {
		log.info("Searching stops for: " + q);
		List<Stop> results = new ArrayList<Stop>();

		final boolean isStopId = q.matches("\\d\\d\\d\\d\\d");
		if (isStopId) {
			final Stop stop = stopDAO.getStop(Integer.parseInt(q));
			if (stop != null) {
				log.info("Found stop by id: " + stop.getName());
				results.add(stop);			
			}
		}
		return results;
	}
	
	public List<Stop> findStopsNear(double latitude, double longitude) {		
		return stopDAO.findStopsNear(latitude, longitude);
	}

	public Set<Route> findRoutesNear(double latitude, double longitude) {
		final Set<Route> routes = new HashSet<Route>();
		for (Stop stop : stopDAO.findStopsNear(latitude, longitude)) {
			routes.addAll(stop.getRoutes());
		}
		return routes;
	}

	public List<Stop> findStopsForRoute(String route, int run) {
		final List<Stop> stops = new ArrayList<Stop>();
		for (RouteStop routeStop : routeStopDAO.findByRoute(route, run)) {
			stops.add(getStopById(routeStop.getBus_Stop_Code()));
		}
		return stops;
	}

	public Stop makeStopFromRouteStop(final RouteStop routeStop) {
		return new Stop(routeStop.getBus_Stop_Code(), routeStop.getStop_Name(), null, null, routeStop.getLatitude(), routeStop.getLongitude(), routeStop.isNationalRail(), routeStop.isTube(), routeStop.isTram());
	}
	
}
