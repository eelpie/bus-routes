package uk.co.eelpieconsulting.busroutes.timers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import uk.co.eelpieconsulting.busroutes.parsing.RouteFileFinderService;
import uk.co.eelpieconsulting.busroutes.parsing.RouteImportService;
import uk.co.eelpieconsulting.common.http.HttpBadRequestException;
import uk.co.eelpieconsulting.common.http.HttpFetchException;
import uk.co.eelpieconsulting.common.http.HttpFetcher;
import uk.co.eelpieconsulting.common.http.HttpForbiddenException;
import uk.co.eelpieconsulting.common.http.HttpNotFoundException;

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
	
	@Scheduled(fixedDelay=30000)
	public void importRoutes() throws SolrServerException, IOException {
		log.info("Starting route import");
		
		final File routesFile = routeFileFinderService.findRoutesFile();
		downloadRoutesFileUpdateTo(routesFile);
		
		if (isUpdate(routesFile)) {
			log.info("Routes file is an update; importing: " + routesFile.getAbsolutePath());
			routeImportService.importRoutes(routesFile);
			log.info("Finished route import");
		} else {
			log.info("Routes file is the same as previously imported; not importing: " + routesFile.getAbsolutePath());
		}
	}
	
	private void downloadRoutesFileUpdateTo(File routesFile) {
		log.info("Downloading routes file update");
		
		try {
			IOUtils.write(new HttpFetcher().getBytes(routeFileFinderService.routesFileFeedUrl()), new FileOutputStream(routesFile));
			log.info("Routes file downloaded");
			
		} catch (HttpNotFoundException e) {
			log.error(e);
			throw new RuntimeException(e);
		} catch (HttpBadRequestException e) {
			log.error(e);
			throw new RuntimeException(e);
		} catch (HttpForbiddenException e) {
			log.error(e);
			throw new RuntimeException(e);
		} catch (HttpFetchException e) {
			log.error(e);
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			log.error(e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	private boolean isUpdate(File routesFile) {
		return true;	// TODO implement
	}
	
}
