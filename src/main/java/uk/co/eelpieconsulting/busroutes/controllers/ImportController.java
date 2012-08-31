package uk.co.eelpieconsulting.busroutes.controllers;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.co.eelpieconsulting.busroutes.parsing.RouteImportService;
import uk.co.eelpieconsulting.busroutes.services.solr.SolrUpdateService;
import uk.co.eelpieconsulting.common.views.ViewFactory;

@Controller
public class ImportController {
	
	private final ViewFactory viewFactory;
	private final RouteImportService routeImportService;
	private final SolrUpdateService solrUpdateService;
	
	@Autowired
	public ImportController(RouteImportService routeImportService, SolrUpdateService solrUpdateService, ViewFactory viewFactory) {
		this.routeImportService = routeImportService;
		this.solrUpdateService = solrUpdateService;
		this.viewFactory = viewFactory;
	}
	
	@RequestMapping("/import")
	public ModelAndView importRoutes() throws InterruptedException, SolrServerException, IOException {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView());
		routeImportService.importRoutes();
		return mv;
	}
	
	@RequestMapping("/solr/update")
	public ModelAndView updateSolr() throws SolrServerException, IOException {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView());
		solrUpdateService.updateSolr();
		return mv;
	}
	
}
