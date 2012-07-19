package uk.co.eelpieconsulting.busroutes.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.daos.RouteStopDAO;
import uk.co.eelpieconsulting.busroutes.model.Route;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;
import uk.co.eelpieconsulting.busroutes.model.Stop;

@Component
public class StopsService {

	private RouteStopDAO routeStopDAO;
	
	public StopsService() {
	}

	@Autowired
	public StopsService(RouteStopDAO routeDAO) {
		this.routeStopDAO = routeDAO;
	}

	public Set<Stop> findStopsNear(double latitude, double longitude) {
		final Map<Integer, Stop> stops = new HashMap<Integer, Stop>();
		
		for (RouteStop routeStop : routeStopDAO.findNear(latitude, longitude)) {
			Stop stop = stops.get(routeStop.getBus_Stop_Code());
			if (stop == null) {
				stop = new Stop(routeStop.getBus_Stop_Code(), routeStop.getStop_Name(), routeStop.getLatitude(), routeStop.getLongitude(), routeStop.isNationalRail(), routeStop.isTube());
				stops.put(routeStop.getBus_Stop_Code(), stop);
			}
			stop.addRoute(new Route(routeStop.getRoute(), routeStop.getRun()));						
		}
		
		return new HashSet<Stop>(stops.values());
	}

	public Set<Route> findRoutesNear(double latitude, double longitude) {
		final Set<Route> routes = new HashSet<Route>();
		for (RouteStop routeStop : routeStopDAO.findNear(latitude, longitude)) {
			routes.add(new Route(routeStop.getRoute(), routeStop.getRun()));
		}
		return routes;
	}

	public List<Stop> findStopsForRoute(String route, int run) {
		final List<Stop> stops = new ArrayList<Stop>();
		for (RouteStop routeStop : routeStopDAO.findByRoute(route, run)) {
			Stop stop = new Stop(routeStop.getBus_Stop_Code(), routeStop.getStop_Name(), routeStop.getLatitude(), routeStop.getLongitude(), routeStop.isNationalRail(), routeStop.isTube());
			stops.add(stop);
		}
		return stops;
	}
	
}
