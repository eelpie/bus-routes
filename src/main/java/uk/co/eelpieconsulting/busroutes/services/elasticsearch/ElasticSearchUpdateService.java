package uk.co.eelpieconsulting.busroutes.services.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.daos.StopDAO;
import uk.co.eelpieconsulting.busroutes.model.PersistedStop;

@Component
public class ElasticSearchUpdateService {
	
	private final StopDAO stopsDAO;
	private final ElasticSearchIndexUpdateService elasticSearchIndexUpdateService;
	
	@Autowired
	public ElasticSearchUpdateService(StopDAO stopsService, ElasticSearchIndexUpdateService elasticSearchIndexUpdateService) {
		this.stopsDAO = stopsService;
		this.elasticSearchIndexUpdateService = elasticSearchIndexUpdateService;
	}
	
	public void updateIndex() {		
		for (PersistedStop stop : stopsDAO.getAll()) {
			elasticSearchIndexUpdateService.updateSingleContentItem(stop);
		}
	}
	
}
