package uk.co.eelpieconsulting.busroutes.parsing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.daos.RouteStopDAO;
import uk.co.eelpieconsulting.busroutes.daos.StopDAO;
import uk.co.eelpieconsulting.busroutes.model.PersistedStop;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;
import uk.co.eelpieconsulting.busroutes.model.Stop;
import uk.co.eelpieconsulting.busroutes.services.StopsService;
import uk.co.eelpieconsulting.countdown.api.CountdownApi;

@Component
public class RouteImportService {

	private static Logger log = Logger.getLogger(RouteImportService.class);
	
	private final static int API_STOPS_FETCH_SIZE = 20;
	private final static int RETRY_BATCH_SIZE = 10;
	private final static int REQUEST_WAIT = 1000;
		
	private RoutesParser routesParser;
	private RouteStopDAO routeStopDAO;
	private StopsService stopsService;
	private StopDAO stopDAO;
	private CountdownApi countdownApi;

	@Value("#{busRoutes['routeFilePath']}")
	private String routeFilePath;
	
	public RouteImportService() {
	}
		
	@Autowired	
	public RouteImportService(RoutesParser routesParser, RouteStopDAO routeStopDAO, StopDAO stopDAO, StopsService stopsService) {
		this.routesParser = routesParser;
		this.routeStopDAO = routeStopDAO;
		this.stopDAO = stopDAO;
		this.stopsService = stopsService;
		
		countdownApi = new CountdownApi("http://countdown.api.tfl.gov.uk");
	}

	public void importRoutes() throws InterruptedException, FileNotFoundException {
		log.info("Reading route data from file: " + routeFilePath);
		final InputStream input = new FileInputStream(routeFilePath);
		
		log.info("Purging existing stop data");
		removeExisting();
		
		log.info("Importing new route/stop rows");
		final List<Integer> stopIds = importRouteStops(routesParser.parseRoutesFile(input));
		Collections.sort(stopIds);
		
		log.info("Created stop rows");
		makeStops(stopIds);
		
		infillStopDetailsFromArrivalsAPI(new ArrayList<Integer>(stopIds));
	}

	private void infillStopDetailsFromArrivalsAPI(List<Integer> stopIdsList) throws InterruptedException {
		log.info("Infilling stop details from arrivals api");
		List<Integer> failed = fetchStopDetails(stopIdsList, API_STOPS_FETCH_SIZE);
		if (failed.isEmpty()) {
			System.out.println("Done");
			return;
		}
		
		log.info("Retrying failed batches with small batch sizes: " + RETRY_BATCH_SIZE);
		failed = fetchStopDetails(failed, RETRY_BATCH_SIZE);
		
		log.info("Retrying failed ids individually");
		failed = fetchStopDetails(failed, 1);
		
		log.info("Done");
		if (failed.isEmpty()) {
			log.info("All stops successfully fetched");
			return;
		}
		
		log.info("Number of unrecoverable failed ids: " + failed.size());
		log.info("Unrecoverable failed ids: " + failed);
	}

	private void makeStops(List<Integer> stopIds) {
		final int size = stopIds.size();
		int count = 1;
		for (Integer stopId : stopIds) {
			RouteStop routeStop = routeStopDAO.getFirstForStopId(stopId);
			Stop stop = stopsService.makeStopFromRouteStop(routeStop);
			log.info(count + "/" + size + " - Creating stop: " + stop.getName());
			stopDAO.saveStop(new PersistedStop(stop));
			count++;
		}
	}

	private List<Integer> importRouteStops(final List<RouteStop> routeStops) {
		log.info("Importing RouteStop rows: " + routeStops.size());
		final Set<Integer> stopIds = new HashSet<Integer>();
		for (RouteStop routeStop : routeStops) {
			routeStopDAO.addRouteStop(routeStop);
			stopIds.add(routeStop.getBus_Stop_Code());			
		}
		
		return new ArrayList<Integer>(stopIds);		
	}

	private void removeExisting() {
		routeStopDAO.removeAll();
		stopDAO.removeAllStops();
	}

	private List<Integer> fetchStopDetails(List<Integer> stopIdsList, int batchSize) throws InterruptedException {
		log.info("Fetching in batches of " + API_STOPS_FETCH_SIZE);

		final List<Integer> failedIds = new ArrayList<Integer>();
		for (int i = 0; i < stopIdsList.size(); i = i + batchSize) {
			List<Integer> subList = stopIdsList.subList(i, i + batchSize < stopIdsList.size() ? i + batchSize : stopIdsList.size() -1);			
			int j = 1;
			final String progress = (i + j) + "/" + stopIdsList.size();
			try {
				final List<Stop> stopDetails = countdownApi.getStopDetails(subList);
				for (Stop apiStop : stopDetails) {
					final Stop stop = stopDAO.getStop(apiStop.getId());
					stop.setName(apiStop.getName());
					stop.setIndicator(apiStop.getIndicator());
					stop.setTowards(apiStop.getTowards());			
					
					log.info(progress + " - Fetched details of stop: " + stop.getName());
					stopDAO.saveStop(new PersistedStop(stop));
				}
				
			} catch (Exception e) {
				log.warn(progress + " - Recording failed batch (one of these stops is considered invalid by the arrivals api): " + subList);
				failedIds.addAll(subList);
			}
			j++;
			Thread.sleep(REQUEST_WAIT);
		}
		return failedIds;
	}
	
}
