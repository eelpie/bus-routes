package uk.co.eelpieconsulting.busroutes.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.daos.RouteStopDAO;
import uk.co.eelpieconsulting.busroutes.daos.StopDAO;
import uk.co.eelpieconsulting.busroutes.model.Message;
import uk.co.eelpieconsulting.busroutes.model.Route;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;
import uk.co.eelpieconsulting.busroutes.model.Stop;
import uk.co.eelpieconsulting.countdown.api.CountdownApi;
import uk.co.eelpieconsulting.countdown.exceptions.HttpFetchException;
import uk.co.eelpieconsulting.countdown.exceptions.ParsingException;
import uk.co.eelpieconsulting.countdown.model.StopBoard;

@Component
public class StopsService {

	private RouteStopDAO routeStopDAO;
	private StopDAO stopDAO;
	private CountdownApi countdownApi;
	
	public StopsService() {
	}

	@Autowired
	public StopsService(RouteStopDAO routeDAO, StopDAO stopDAO) {
		this.routeStopDAO = routeDAO;
		this.stopDAO = stopDAO;
		countdownApi = new CountdownApi("http://countdown.api.tfl.gov.uk");	// TODO inject
	}
	
	public Stop getStopById(int id) {
		return stopDAO.getStop(id);
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

	public StopBoard getStopBoard(int stopId) {
		// TODO Scavenge stop indicator and towards labels for stops from the arrival stopboard.
		// These fields are not available in the stop and route data files.
		try {
			return countdownApi.getStopBoard(stopId);
		} catch (HttpFetchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}
	
	public List<Message> getStopMessages(int stopId) {
		try {
			return countdownApi.getStopMessages(stopId);
		} catch (HttpFetchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
