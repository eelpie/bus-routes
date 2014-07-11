package uk.co.eelpieconsulting.busroutes.services.elasticsearch;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.model.Stop;

public class StopSearchServiceIT {
	
	@Test
	public void canSearchForStopsByName() throws Exception {		
		final StopSearchService stopSearchService = new StopSearchService();
		
		final List<Stop> stopsMatching = stopSearchService.stopsMatching("Twickenham Station");		
		
		for (Stop stop : stopsMatching) {
			System.out.println(stop.getName());			
		}		
		assertFalse(stopsMatching.isEmpty());
	}
	
}
