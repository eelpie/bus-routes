package uk.co.eelpieconsulting.busroutes.parsing;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.eelpieconsulting.busroutes.daos.RouteStopDAO;
import uk.co.eelpieconsulting.busroutes.daos.StopDAO;
import uk.co.eelpieconsulting.busroutes.model.PersistedStop;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;
import uk.co.eelpieconsulting.busroutes.model.Stop;
import uk.co.eelpieconsulting.busroutes.services.StopsService;

public class RouteImportService {

	private RoutesParser routesParser;
	private RouteStopDAO routeStopDAO;
	private StopsService stopsService;
	private final StopDAO stopDAO;
		
	public RouteImportService(RoutesParser routesParser, RouteStopDAO routeDAO, StopDAO stopDAO, StopsService stopsService) {
		this.routesParser = routesParser;
		this.routeStopDAO = routeDAO;
		this.stopDAO = stopDAO;
		this.stopsService = stopsService;
	}

	public void importRoutes() {
		final InputStream input = this.getClass().getClassLoader().getResourceAsStream("routes.csv");
		final List<RouteStop> routeStops = routesParser.parseRoutesFile(input);
		
		routeStopDAO.removeAll();
		stopDAO.removeAllStops();

		Set<Integer> stopIds = new HashSet<Integer>();
		for (RouteStop routeStop : routeStops) {
			routeStopDAO.addRouteStop(routeStop);
			stopIds.add(routeStop.getBus_Stop_Code());			
		}
		
		final int size = stopIds.size();
		System.out.println(size);
		int i = 1;
		for (Integer stopId : stopIds) {
			RouteStop routeStop = routeStopDAO.getFirstForStopId(stopId);
			Stop stop = stopsService.makeStopFromRouteStop(routeStop);
			System.out.println(i + "/" + size + " - " + stop);
			stopDAO.saveStop(new PersistedStop(stop));
			i++;
		}		
	}
	
}
