package uk.co.eelpieconsulting.busroutes.services.elasticsearch;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.Stop;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class StopSearchService {
		
	private static final int MAXIMUM_SEARCH_RESULTS = 50;
	
	private final ElasticSearchClientFactory elasticSearchClientFactory;
	private final ObjectMapper mapper;

	@Autowired
	public StopSearchService(ElasticSearchClientFactory elasticSearchClientFactory) {
		this.elasticSearchClientFactory = elasticSearchClientFactory;
		this.mapper = new ObjectMapper();
	    this.mapper.configure(MapperFeature.USE_ANNOTATIONS, true);	 
	}
	
	public List<Stop> stopsMatching(String q) throws JsonParseException, JsonMappingException, IOException {
		final MatchQueryBuilder termQuery = QueryBuilders.matchQuery("name", q);
		final SearchRequestBuilder searchRequestBuilder = searchRequestBuilder()
				.setQuery(termQuery)
				.setMinScore(1)
				.setSize(MAXIMUM_SEARCH_RESULTS);

		final SearchResponse response = searchRequestBuilder.execute().actionGet();
		return deserializeFrontendResourceHits(response.getHits());	
	}
	
	 private SearchRequestBuilder searchRequestBuilder() {
         return elasticSearchClientFactory.getClient().prepareSearch().
         	setIndices(ElasticSearchIndexUpdateService.INDEX).
         	setTypes(ElasticSearchIndexUpdateService.TYPE);
	 }
	 
	 private List<Stop> deserializeFrontendResourceHits(SearchHits hits) throws JsonParseException, JsonMappingException, IOException {
         final List<Stop> resources = Lists.newArrayList();
         final Iterator<SearchHit> iterator = hits.iterator();
         while (iterator.hasNext()) {
                 final SearchHit next = iterator.next();
                 resources.add(mapper.readValue(next.getSourceAsString(), Stop.class));                 
         }
         return resources;
	 }
	 
}
