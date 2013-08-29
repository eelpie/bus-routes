package uk.co.eelpieconsulting.busroutes.timers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import uk.co.eelpieconsulting.busroutes.daos.RoutesFileChecksumDAO;
import uk.co.eelpieconsulting.busroutes.parsing.RouteFileFinderService;
import uk.co.eelpieconsulting.busroutes.parsing.RouteImportService;
import uk.co.eelpieconsulting.common.files.FileInformationService;
import uk.co.eelpieconsulting.common.files.model.FileInformation;

@Service
public class ImportStopsTask {
	
	private static Logger log = Logger.getLogger(ImportStopsTask.class);
	
	private RouteImportService routeImportService;
	private RouteFileFinderService routeFileFinderService;
	private RoutesFileChecksumDAO routesFileChecksumDAO;
	private FileInformationService fileInformationService;
	
	public ImportStopsTask() {
	}
	
	@Autowired
	public ImportStopsTask(RouteImportService routeImportService, RouteFileFinderService routeFileFinderService, 
			RoutesFileChecksumDAO routesFileChecksumDAO) {
		this.routeImportService = routeImportService;
		this.routeFileFinderService = routeFileFinderService;
		this.routesFileChecksumDAO = routesFileChecksumDAO;
		this.fileInformationService = new FileInformationService();
	}
	
	@Scheduled(fixedDelay = 60000)
	public void importRoutes() throws IOException {
		final File routesFile = routeFileFinderService.findRoutesFile();		
		log.info("Starting route import from filepath: " + routesFile.getAbsolutePath());
		
		if (isUpdate(routesFile)) {
			log.info("Routes file is an update; importing: " + routesFile.getAbsolutePath());
			routeImportService.importRoutes(routesFile);
			log.info("Finished route import");
			
		} else {
			log.info("Routes file is the same as previously imported; not importing: " + routesFile.getAbsolutePath());
		}
	}
	
	private boolean isUpdate(File routesFile) throws FileNotFoundException, IOException {
		final FileInformation fileInformation = fileInformationService.getFileInformation(routesFile);
		log.info("Routes file information: " + fileInformation);
		
		final String existingChecksum = routesFileChecksumDAO.getChecksum();
		log.info("Last imported checksum is: " + existingChecksum);
		if (existingChecksum == null) {
			return true;
		}
		
		return !fileInformation.getMd5().equals(existingChecksum);		
	}
	
}
