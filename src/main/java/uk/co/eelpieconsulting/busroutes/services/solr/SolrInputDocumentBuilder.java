package uk.co.eelpieconsulting.busroutes.services.solr;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.Stop;

@Component
public class SolrInputDocumentBuilder {
	
	public SolrInputDocument makeSolrDocumentFor(Stop stop) {
		SolrInputDocument inputDocument = new SolrInputDocument();
		inputDocument.addField("id", stop.getId());
		inputDocument.addField("name", stop.getName());		
        return inputDocument;
	}
	
}
