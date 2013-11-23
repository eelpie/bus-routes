package uk.co.eelpieconsulting.busroutes.parsing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;
import org.elasticsearch.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.RouteStop;

@Component
public class RoutesParser {
	
	private static Logger log = Logger.getLogger(RoutesParser.class);

	private static final String UTF_8 = "UTF-8";

	private final RouteLineParser routeLineParser;
	
	@Autowired
	public RoutesParser(RouteLineParser routeLineParser) {
		this.routeLineParser = routeLineParser;
	}
	
	public List<RouteStop> parseRoutesFile(InputStream routesFile) {
		log.info("Parsing input");
		final List<RouteStop> routeStops = Lists.newArrayList();
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(routesFile, UTF_8));
			
			reader.readLine();
			while (reader.ready()) {			
				final String line = reader.readLine();
				try {
					routeStops.add(routeLineParser.parseRouteStopLine(line));
				} catch (Exception e) {
					log.warn(e.getMessage());
				}
			}
			
			reader.close();
			routesFile.close();

		} catch (Exception e) {
			log.error(e);
		}
		
		log.info("Parsed " + routeStops.size() + " lines");
		return routeStops;
	}

}
