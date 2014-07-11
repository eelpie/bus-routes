package uk.co.eelpieconsulting.busroutes.services.elasticsearch;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.Stop;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Lists;

@Component
public class StopSearchService {
	
	public List<Stop> stopsMatching(String q) throws JsonParseException, JsonMappingException, IOException {
		return Lists.newArrayList();	// TODO implement as Mondgo text index.
	}
	 
}
