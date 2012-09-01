package uk.co.eelpieconsulting.busroutes.parsing;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.model.RouteStop;

public class RoutesParserTest {

	private RoutesParser parser;

	@Before
	public void setup() {
		parser = new RoutesParser(new RouteLineParser(new EastingsNorthingsConvertor()));
	}
	
	@Test
	public void canParseRoutesFile() throws Exception {
		final List<RouteStop> routeStops = parser.parseRoutesFile(getTestRoutesFile());
		
		for (RouteStop routeStop : routeStops) {
			if (routeStop.getRoute().equals("N22")) {
				System.out.println(routeStop);
				return;
			}
		}
		
		fail();
	}

	@Test
	public void virtualStopsShouldBeMarkedAsSuch() throws Exception {		
		final List<RouteStop> routeStops = parser.parseRoutesFile(getTestRoutesFile());
		
		final List<Integer> knownVirtualStops = Arrays.asList(new Integer[] {56346, 56415, 56426, 56464, 56495, 56553, 56788, 56797, 56808, 56876, 56932, 56942, 56983, 56995, 56999, 57004, 57018, 57124, 57238, 57239, 57255, 57299, 57338, 57394, 57399, 57401, 57442, 57495, 57503});
		for (RouteStop routeStop : routeStops) {
			if (knownVirtualStops.contains(routeStop.getBus_Stop_Code())) {
				assertTrue(routeStop.getVirtual_Bus_Stop());
			}
		}
	}
	
	private File getTestRoutesFile() {
		return new File(this.getClass().getClassLoader().getResource("routes.csv").getFile());
	}
	

}
