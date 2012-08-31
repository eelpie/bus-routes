package uk.co.eelpieconsulting.busroutes.services.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.daos.StopDAO;
import uk.co.eelpieconsulting.busroutes.model.PersistedStop;

@Component
public class SolrUpdateService {
	
	private final StopDAO stopsDAO;
	private final SolrInputDocumentBuilder solrInputDocumentBuilder;
	private final SolrServer solrServer;
	
	@Autowired
	public SolrUpdateService(StopDAO stopsService, SolrInputDocumentBuilder solrInputDocumentBuilder, SolrServer solrServer) {
		this.stopsDAO = stopsService;
		this.solrInputDocumentBuilder = solrInputDocumentBuilder;
		this.solrServer = solrServer;
	}
	
	// TODO push to service and call as part of the import
	public void updateSolr() throws SolrServerException, IOException {
		final UpdateRequest updateRequest = new UpdateRequest();
		
		for (PersistedStop stop : stopsDAO.getAll()) {
			updateRequest.add(solrInputDocumentBuilder.makeSolrDocumentFor(stop));
		}
		
		updateRequest.process(solrServer);
		solrServer.commit();
		solrServer.optimize();
	}
	
}
