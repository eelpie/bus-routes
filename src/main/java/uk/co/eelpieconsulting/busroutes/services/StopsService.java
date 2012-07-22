package uk.co.eelpieconsulting.busroutes.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	
	public Stop getStopById(int id) {
		return routeStopDAO.getStop(id);
	}
	
	public List<Stop> findStopsNear(double latitude, double longitude) {		
		return routeStopDAO.findStopsNear(latitude, longitude);
	}

	public Set<Route> findRoutesNear(double latitude, double longitude) {
		final Set<Route> routes = new HashSet<Route>();
		for (RouteStop routeStop : routeStopDAO.findNear(latitude, longitude)) {
			routes.add(new Route(routeStop.getRoute(), routeStop.getRun(), getDestinationFor(routeStop.getRoute(), routeStop.getRun())));
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
		Stop stop = new Stop(routeStop.getBus_Stop_Code(), routeStop.getStop_Name(), null, null, routeStop.getLatitude(), routeStop.getLongitude(), routeStop.isNationalRail(), routeStop.isTube());
		decorateStopWithRoutes(stop);
		return stop;		
	}
	
	private void decorateStopWithRoutes(final Stop stop) {
		for (RouteStop stopRouteStop : routeStopDAO.findByStopId(stop.getId())) {
			stop.addRoute(new Route(stopRouteStop.getRoute(), stopRouteStop.getRun(), getDestinationFor(stopRouteStop.getRoute(), stopRouteStop.getRun())));			
		}
	}
	
	private String getDestinationFor(String route, int run) {
		return routeStopDAO.findLastForRoute(route, run).getStop_Name();
	}
	
}
