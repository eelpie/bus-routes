package uk.co.eelpieconsulting.busroutes.parsing;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.geo.OSRefConvertor;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;

public class RoutesParserTest {

	@Test
	public void canParseRoutesFile() throws Exception {
		final InputStream input = this.getClass().getClassLoader().getResourceAsStream("routes.csv");			
		
		RoutesParser parser = new RoutesParser(new OSRefConvertor());
		
		final List<RouteStop> routeStops = parser.parseRoutesFile(input);
		for (RouteStop routeStop : routeStops) {
			if (routeStop.getRoute().equals("N22")) {
				System.out.println(routeStop);
			}
		}
	}

}
