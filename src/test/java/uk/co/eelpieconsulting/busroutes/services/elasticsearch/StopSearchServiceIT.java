package uk.co.eelpieconsulting.busroutes.services.elasticsearch;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.model.Stop;
import uk.co.eelpieconsulting.busroutes.services.elasticsearch.ElasticSearchClientFactory;
import uk.co.eelpieconsulting.busroutes.services.elasticsearch.StopSearchService;


public class StopSearchServiceIT {
	
	@Test
	public void canSearchForStopsByName() throws Exception {		
		final ElasticSearchClientFactory elasticSearchClientFactory = new ElasticSearchClientFactory("elasticsearch", "ubuntu.local");		
		final StopSearchService stopSearchService = new StopSearchService(elasticSearchClientFactory);
		
		final List<Stop> stopsMatching = stopSearchService.stopsMatching("Twickenham Station");		
		
		for (Stop stop : stopsMatching) {
			System.out.println(stop.getName());			
		}		
		assertFalse(stopsMatching.isEmpty());
	}
	
}
