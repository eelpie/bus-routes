package uk.co.eelpieconsulting.busroutes.services.elasticsearch;

import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.Stop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ElasticSearchIndexUpdateService {

	public static final String INDEX = "buses";	// TODO config
	public static final String TYPE = "stops";	// TODO config
	
	private static Logger log = Logger.getLogger(ElasticSearchIndexUpdateService.class);
	
	private final ElasticSearchClientFactory elasticSearchClientFactory;
	private final ObjectMapper mapper;

	@Autowired
	public ElasticSearchIndexUpdateService(ElasticSearchClientFactory elasticSearchClientFactory) {
		this.elasticSearchClientFactory = elasticSearchClientFactory;
		this.mapper = new ObjectMapper();
	    this.mapper.configure(MapperFeature.USE_ANNOTATIONS, true);	    
	}
	
	public void updateSingleContentItem(Stop stop) {
		log.debug("Updating stop: " + stop.getId());		
		try {
			final Client client = elasticSearchClientFactory.getClient();
			prepateUpdateFor(stop, client).execute().actionGet();			
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void deleteContentItem(Integer id) {
		log.info("Deleting stop: " + id);
		elasticSearchClientFactory.getClient().prepareDelete(INDEX, TYPE, Integer.toString(id)).setOperationThreaded(false).execute().actionGet();
	}
	
	private IndexRequestBuilder prepateUpdateFor(Stop stop, Client client) throws JsonProcessingException {				
		final String json = mapper.writeValueAsString(stop);
		log.debug("Updating elastic search with json: " + json);
		return client.prepareIndex(INDEX, TYPE, Integer.toString(stop.getId())).setSource(json);
	}
	
}
