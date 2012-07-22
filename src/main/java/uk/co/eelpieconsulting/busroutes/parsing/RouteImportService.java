package uk.co.eelpieconsulting.busroutes.parsing;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.eelpieconsulting.busroutes.daos.RouteStopDAO;
import uk.co.eelpieconsulting.busroutes.daos.StopDAO;
import uk.co.eelpieconsulting.busroutes.model.PersistedStop;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;
import uk.co.eelpieconsulting.busroutes.model.Stop;
import uk.co.eelpieconsulting.busroutes.services.StopsService;
import uk.co.eelpieconsulting.countdown.api.CountdownApi;
import uk.co.eelpieconsulting.countdown.exceptions.HttpFetchException;
import uk.co.eelpieconsulting.countdown.exceptions.ParsingException;

public class RouteImportService {

	private static final int API_STOPS_FETCH_SIZE = 10;
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

	public void importRoutes() throws InterruptedException {
		final InputStream input = this.getClass().getClassLoader().getResourceAsStream("routes.csv");
		final List<RouteStop> routeStops = routesParser.parseRoutesFile(input);
		
		routeStopDAO.removeAll();
		stopDAO.removeAllStops();		

		System.out.println("Importing RouteStop rows");
		Set<Integer> stopIds = new HashSet<Integer>();
		for (RouteStop routeStop : routeStops) {
			routeStopDAO.addRouteStop(routeStop);
			stopIds.add(routeStop.getBus_Stop_Code());			
		}
				
		System.out.println("Making stops");
		final int size = stopIds.size();
		System.out.println(size);
		int count = 1;
		for (Integer stopId : stopIds) {
			RouteStop routeStop = routeStopDAO.getFirstForStopId(stopId);
			Stop stop = stopsService.makeStopFromRouteStop(routeStop);
			System.out.println(count + "/" + size + " - " + stop);
			stopDAO.saveStop(new PersistedStop(stop));
			count++;
		}
		
		System.out.println("Infilling stop details from arrivals api");
		final CountdownApi countdownApi = new CountdownApi("http://countdown.api.tfl.gov.uk");		
		List<Integer> stopIdsList = new ArrayList<Integer>();
		stopIdsList.addAll(stopIds);
		for (int i = 0; i < stopIds.size(); i = i + API_STOPS_FETCH_SIZE) {
			List<Integer> subList = stopIdsList.subList(i, i + API_STOPS_FETCH_SIZE < stopIds.size() ? i + API_STOPS_FETCH_SIZE : stopIds.size() -1);
			
			List<Stop> stopDetails;
			try {
				stopDetails = countdownApi.getStopDetails(subList);			
				for (Stop apiStop : stopDetails) {
					Stop stop = stopDAO.getStop(apiStop.getId());
					stop.setName(apiStop.getName());
					stop.setIndicator(apiStop.getIndicator());
					stop.setTowards(apiStop.getTowards());			
					System.out.println(i + "/" + stopIds.size() + ":" + stop);
					stopDAO.saveStop(new PersistedStop(stop));
				}
				
			} catch (HttpFetchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Thread.sleep(1000);
		}
	}
	
}
