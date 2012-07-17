package uk.co.eelpieconsulting.busroutes.services;

import java.util.HashSet;
import java.util.Set;

import uk.co.eelpieconsulting.busroutes.daos.RouteDAO;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;

public class StopsService {

	private final RouteDAO routeDAO;

	public StopsService(RouteDAO routeDAO) {
		this.routeDAO = routeDAO;
	}

	public Set<Integer> findStopsNear(double latitude, double longitude) {
		final Set<Integer> stopIds = new HashSet<Integer>();
		for (RouteStop routeStop : routeDAO.findStopsNear(latitude, longitude)) {
			stopIds.add(routeStop.getBus_Stop_Code());
		}
		return stopIds;
	}

	public Set<String> findRoutesNear(double latitude, double longitude) {
		final Set<String> routes = new HashSet<String>();
		for (RouteStop routeStop : routeDAO.findStopsNear(latitude, longitude)) {
			routes.add(routeStop.getRoute());
		}
		return routes;
	}
	
}
