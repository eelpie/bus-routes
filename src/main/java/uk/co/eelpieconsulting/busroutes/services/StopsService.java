package uk.co.eelpieconsulting.busroutes.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.co.eelpieconsulting.busroutes.daos.RouteDAO;
import uk.co.eelpieconsulting.busroutes.model.Route;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;
import uk.co.eelpieconsulting.busroutes.model.Stop;

public class StopsService {

	private final RouteDAO routeDAO;

	public StopsService(RouteDAO routeDAO) {
		this.routeDAO = routeDAO;
	}

	public Set<Stop> findStopsNear(double latitude, double longitude) {
		final Map<Integer, Stop> stops = new HashMap<Integer, Stop>();
		
		for (RouteStop routeStop : routeDAO.findStopsNear(latitude, longitude)) {
			Stop stop = stops.get(routeStop.getBus_Stop_Code());
			if (stop == null) {
				stop = new Stop(routeStop.getBus_Stop_Code(), routeStop.getStop_Name(), routeStop.getLocation()[0], routeStop.getLocation()[1]);
				stops.put(routeStop.getBus_Stop_Code(), stop);
			}
			stop.addRoute(new Route(routeStop.getRoute(), routeStop.getRun()));						
		}
		
		return new HashSet<Stop>(stops.values());
	}

	public Set<Route> findRoutesNear(double latitude, double longitude) {
		final Set<Route> routes = new HashSet<Route>();
		for (RouteStop routeStop : routeDAO.findStopsNear(latitude, longitude)) {
			routes.add(new Route(routeStop.getRoute(), routeStop.getRun()));
		}
		return routes;
	}
	
}
