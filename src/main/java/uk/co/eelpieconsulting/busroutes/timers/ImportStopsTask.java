package uk.co.eelpieconsulting.busroutes.timers;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import uk.co.eelpieconsulting.busroutes.parsing.RouteFileFinderService;
import uk.co.eelpieconsulting.busroutes.parsing.RouteImportService;

@Service
public class ImportStopsTask {
	
	private static Logger log = Logger.getLogger(ImportStopsTask.class);
	
	private RouteImportService routeImportService;
	private RouteFileFinderService routeFileFinderService;
	
	public ImportStopsTask() {
	}
	
	@Autowired
	public ImportStopsTask(RouteImportService routeImportService, RouteFileFinderService routeFileFinderService) {
		this.routeImportService = routeImportService;
		this.routeFileFinderService = routeFileFinderService;
	}
	
	@Scheduled(cron="0 4 * * * SAT")
	public void importRoutes() throws SolrServerException, IOException {
		log.info("Starting route import");
		File routesFile = routeFileFinderService.findRoutesFile();
		if (isUpdate(routesFile)) {
			log.info("Routes file is an update; importing: " + routesFile.getAbsolutePath());
			routeImportService.importRoutes(routesFile);
			log.info("Finished route import");
		} else {
			log.info("Routes file is the same as previously imported; not importing: " + routesFile.getAbsolutePath());
		}
	}
	
	private boolean isUpdate(File routesFile) {
		return true;	// TODO implement
	}
	
}
