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

public class RouteImportService {

	private static final int API_STOPS_FETCH_SIZE = 50;
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
		removeExisting();
		
		final InputStream input = this.getClass().getClassLoader().getResourceAsStream("routes.csv");
		final Set<Integer> stopIds = importRouteStops(routesParser.parseRoutesFile(input));
		
		makeStops(stopIds);
		
		infillStopDetailsFromArrivalsAPI(new ArrayList<Integer>(stopIds));
	}

	private void infillStopDetailsFromArrivalsAPI(List<Integer> stopIdsList) throws InterruptedException {
		System.out.println("Infilling stop details from arrivals api");
		List<Integer> failed = fetchStopDetails(stopIdsList, API_STOPS_FETCH_SIZE);
		if (failed.isEmpty()) {
			System.out.println("Done");
			return;
		}
		
		System.out.println("Retrying failed in half batches: " + API_STOPS_FETCH_SIZE / 2);
		failed = fetchStopDetails(failed, API_STOPS_FETCH_SIZE / 2);
		
		System.out.println("Retrying failed ids individually");
		failed = fetchStopDetails(failed, 1);
		
		if (failed.isEmpty()) {
			System.out.println("Done");
			return;
		}
		
		System.out.println("Unrecoverable failed ids: " + failed);
	}

	private void makeStops(Set<Integer> stopIds) {
		System.out.println("Making stops");
		final int size = stopIds.size();
		System.out.println(size);
		int count = 1;
		for (Integer stopId : stopIds) {
			RouteStop routeStop = routeStopDAO.getFirstForStopId(stopId);
			Stop stop = stopsService.makeStopFromRouteStop(routeStop);
			System.out.println(count + "/" + size + " - " + stop.getName());
			stopDAO.saveStop(new PersistedStop(stop));
			count++;
		}
	}

	private Set<Integer> importRouteStops(final List<RouteStop> routeStops) {
		System.out.println("Importing RouteStop rows: " + routeStops.size());
		Set<Integer> stopIds = new HashSet<Integer>();
		for (RouteStop routeStop : routeStops) {
			routeStopDAO.addRouteStop(routeStop);
			stopIds.add(routeStop.getBus_Stop_Code());			
		}
		return stopIds;
	}

	private void removeExisting() {
		routeStopDAO.removeAll();
		stopDAO.removeAllStops();
	}

	private List<Integer> fetchStopDetails(List<Integer> stopIdsList, int batchSize) throws InterruptedException {
		System.out.println("Fetching in batches of " + API_STOPS_FETCH_SIZE);

		final CountdownApi countdownApi = new CountdownApi("http://countdown.api.tfl.gov.uk");	// TODO inject		
		List<Integer> failedIds = new ArrayList<Integer>();
		for (int i = 0; i < stopIdsList.size(); i = i + batchSize) {
			List<Integer> subList = stopIdsList.subList(i, i + batchSize < stopIdsList.size() ? i + batchSize : stopIdsList.size() -1);			
			try {
				List<Stop> stopDetails = countdownApi.getStopDetails(subList);			
				for (Stop apiStop : stopDetails) {
					Stop stop = stopDAO.getStop(apiStop.getId());
					stop.setName(apiStop.getName());
					stop.setIndicator(apiStop.getIndicator());
					stop.setTowards(apiStop.getTowards());			
					System.out.println(i + "/" + stopIdsList.size() + ":" + stop);
					stopDAO.saveStop(new PersistedStop(stop));
				}
				
			} catch (Exception e) {
				System.out.println("Recording failed batch (one of these input is invalid): " + subList);
				failedIds.addAll(subList);
			}
			Thread.sleep(500);
		}
		return failedIds;
	}
	
}
