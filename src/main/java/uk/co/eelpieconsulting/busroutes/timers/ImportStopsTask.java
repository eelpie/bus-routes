package uk.co.eelpieconsulting.busroutes.timers;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Controller;

import uk.co.eelpieconsulting.busroutes.parsing.RouteFileFinderService;
import uk.co.eelpieconsulting.busroutes.parsing.RouteImportService;

@Controller
public class ImportStopsTask {
	
	private static Logger log = Logger.getLogger(ImportStopsTask.class);
	
	private final RouteImportService routeImportService;
	private RouteFileFinderService routeFileFinderService;
	
	public ImportStopsTask(RouteImportService routeImportService, RouteFileFinderService routeFileFinderService) {
		this.routeImportService = routeImportService;
		this.routeFileFinderService = routeFileFinderService;
	}
	
	public void importRoutes() throws SolrServerException, IOException {
		File routesFile = routeFileFinderService.findRoutesFile();
		if (isUpdate(routesFile)) {
			log.info("Routes file is an update; importing: " + routesFile.getAbsolutePath());
			routeImportService.importRoutes(routesFile);
		} else {
			log.info("Routes file is the same as previously imported; not importing: " + routesFile.getAbsolutePath());
		}
	}
	
	private boolean isUpdate(File routesFile) {
		return true;	// TODO implement
	}
	
}
