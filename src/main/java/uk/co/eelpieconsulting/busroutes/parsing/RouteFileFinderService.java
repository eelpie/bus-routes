package uk.co.eelpieconsulting.busroutes.parsing;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RouteFileFinderService {
	
	@Value("#{busRoutes['routeFilePath']}")
	private String routeFilePath;

	public File findRoutesFile() {
		return new File(routeFilePath);
	}

}
