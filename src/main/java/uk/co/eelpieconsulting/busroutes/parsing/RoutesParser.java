package uk.co.eelpieconsulting.busroutes.parsing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import uk.co.eelpieconsulting.busroutes.model.RouteStop;

public class RoutesParser {
	
	private static final String UTF_8 = "UTF-8";

	private final RouteLineParser routeLineParser;
	
	@Autowired
	public RoutesParser(RouteLineParser routeLineParser) {
		this.routeLineParser = routeLineParser;
	}
	
	public List<RouteStop> parseRoutesFile(InputStream input) {
		final List<RouteStop> routeStops = new ArrayList<RouteStop>();
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(input, UTF_8));			

			reader.readLine();
			while (reader.ready()) {			
				final String line = reader.readLine();
				try {
					routeStops.add(routeLineParser.parseRouteStopLine(line));
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
			
			reader.close();
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return routeStops;
	}

}
