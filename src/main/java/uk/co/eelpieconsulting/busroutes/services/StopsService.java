package uk.co.eelpieconsulting.busroutes.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.elasticsearch.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.daos.RouteStopDAO;
import uk.co.eelpieconsulting.busroutes.daos.StopDAO;
import uk.co.eelpieconsulting.busroutes.exceptions.UnknownStopException;
import uk.co.eelpieconsulting.busroutes.model.PersistedStop;
import uk.co.eelpieconsulting.busroutes.model.Route;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;
import uk.co.eelpieconsulting.busroutes.model.Stop;
import uk.co.eelpieconsulting.busroutes.parsing.RouteImportService;
import uk.co.eelpieconsulting.busroutes.services.elasticsearch.StopSearchService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component
public class StopsService {

	private static Logger log = Logger.getLogger(RouteImportService.class);
	
	private RouteStopDAO routeStopDAO;
	private StopDAO stopDAO;

	private final StopSearchService stopSearchService;

	@Autowired
	public StopsService(RouteStopDAO routeDAO, StopDAO stopDAO, StopSearchService stopSearchService) {
		this.routeStopDAO = routeDAO;
		this.stopDAO = stopDAO;
		this.stopSearchService = stopSearchService;
	}
	
	public Stop getStopById(int id) throws UnknownStopException {
		PersistedStop stop = stopDAO.getStop(id);
		if (stop != null) {
			return stop;
		}
		throw new UnknownStopException();
	}
	
	public List<Stop> search(String q) throws JsonParseException, JsonMappingException, IOException {
		log.info("Searching stops for: " + q);
		List<Stop> results = Lists.newArrayList();

		final boolean isStopId = q.matches("\\d\\d\\d\\d\\d");
		log.debug("Is stop id: " + isStopId);
		
		if (isStopId) {
			final Stop stop = stopDAO.getStop(Integer.parseInt(q));
			if (stop != null) {
				log.debug("Found stop by id: " + stop.getName());
				results.add(stop);			
			}
		}
		
		results.addAll(stopSearchService.stopsMatching(q));
		return results;
	}
	
	public List<Stop> findStopsNear(double latitude, double longitude) {		
		return stopDAO.findStopsNear(latitude, longitude);
	}

	public Set<Route> findRoutesNear(double latitude, double longitude) {	// TODO you can probably to this with an elastic search facet
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
