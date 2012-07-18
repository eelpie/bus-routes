package uk.co.eelpieconsulting.busroutes.parsing;

import java.util.List;

import uk.co.eelpieconsulting.busroutes.daos.RouteStopDAO;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;

public class RouteImportService {

	private RoutesParser routesParser;
	private RouteStopDAO routeStopDAO;
		
	public RouteImportService(RoutesParser routesParser, RouteStopDAO routeDAO) {
		this.routesParser = routesParser;
		this.routeStopDAO = routeDAO;
	}

	public void importRoutes() {
		final List<RouteStop> routeStops = routesParser.parseRoutesFile();
		
		routeStopDAO.removeAll();		
		for (RouteStop routeStop : routeStops) {
			routeStopDAO.addRouteStop(routeStop);
		}
	}
	
	
}
