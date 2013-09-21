package uk.co.eelpieconsulting.busroutes.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.Route;
import uk.co.eelpieconsulting.busroutes.services.elasticsearch.ElasticSearchIndexUpdateService;
import uk.co.eelpieconsulting.countdown.model.Arrival;
import uk.co.eelpieconsulting.countdown.model.StopBoard;

@Component
public class AbbreviationCorrectionService {
	
	private static Logger log = Logger.getLogger(ElasticSearchIndexUpdateService.class);
	
	private final Map<String, String> corrections;
	
	public AbbreviationCorrectionService() {
		corrections = new HashMap<String, String>();
		corrections.put("Hounslow Bus St", "Hounslow Bus Station");
		corrections.put("Clapham Jn", "Clapham Junction");
		corrections.put("Liverpool St", "Liverpool Street");
		corrections.put("Notting Hill Ga", "Notting Hill Gate");
		corrections.put("Piccadilly Cir", "Piccadilly Circus");
		corrections.put("St Georges Hosp", "St Georges Hospital");
		corrections.put("Streatham Stn", "Streatham Station");
		corrections.put("Tooting Bdy", "Tooting Broadway");
		corrections.put("Tottenham Ct Rd", "Tottenham Court Road");
		corrections.put("Trafalgar Sq", "Trafalgar Square");
		corrections.put("Tulse Hill Stn", "Tulse Hill Station");
		corrections.put("West Middx Hosp", "West Middlesex Hospital");		 
	}
		
	public StopBoard correctPoorlyAbbrevatedDestinationsInArrivals(StopBoard stopBoard) {
		final List<Arrival> correctedArrivals = new ArrayList<Arrival>();
		for (Arrival arrival : stopBoard.getArrivals()) {			
			final String towards = arrival.getRoute().getTowards();
			log.debug("Arrival destination: " + towards);
			if (corrections.containsKey(towards)) {				
				final Route correctedRoute = new Route(arrival.getRoute().getRoute(), arrival.getRoute().getRun(), corrections.get(towards));
				correctedArrivals.add(new Arrival(correctedRoute, arrival.getEstimatedWait()));
			} else {
				correctedArrivals.add(arrival);
			}
		}
		
		final StopBoard correctedStopBoard = new StopBoard(stopBoard.getLastUpdated(), correctedArrivals);
		return correctedStopBoard;
	}
	
}
