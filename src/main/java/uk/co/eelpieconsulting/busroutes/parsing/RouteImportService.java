package uk.co.eelpieconsulting.busroutes.parsing;

import java.io.InputStream;
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
		final InputStream input = this.getClass().getClassLoader().getResourceAsStream("routes.csv");
		final List<RouteStop> routeStops = routesParser.parseRoutesFile(input);
		
		routeStopDAO.removeAll();		
		for (RouteStop routeStop : routeStops) {
			routeStopDAO.addRouteStop(routeStop);
		}
	}
	
	
}
