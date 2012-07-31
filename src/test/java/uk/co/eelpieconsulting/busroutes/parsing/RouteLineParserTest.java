package uk.co.eelpieconsulting.busroutes.parsing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.co.eelpieconsulting.busroutes.geo.OSRefConvertor;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;

public class RouteLineParserTest {

	private static final String VIRTUAL_STOP = "H2,1,8,H0660,56808,490008800E,KINGSLEY WAY,526148,188175,63,1";
	private static final String LINE_WITH_TUBE_ONLY = "N155,2,46,23927,72538,490000055A,COLLIERS WOOD STATION <>,526811,170413,211,0";
	private static final String LINE_WITH_NATIONAL_RAIL_ONLY = "267,2,22,BP3821,74951,490013861B,TWICKENHAM STATION #,516062,173675,322,0";
	private static final String LINE_WITH_NO_STATIONS = "X68,2,24,13235,50527,490014621S,WHITWORTH ROAD,533365,168673,209,0";
	private static final String LINE_WITH_NATIONAL_RAIL_AND_UNDERGROUND_CHANGES = "R70,2,5,20703,76186,490000192D,RICHMOND STATION <> #,518053,175191,201,0";
	
	private OSRefConvertor osRefConvertor;
	private RouteLineParser parser;

	@Before
	public void setup() {
		osRefConvertor = new OSRefConvertor();
		parser = new RouteLineParser(osRefConvertor);
	}
	
	@Test
	public void canReconigiseNationalRailAndUndergroundHints() throws Exception {

		RouteStop parsedLine = parser.parseRouteStopLine(LINE_WITH_NO_STATIONS);
		assertFalse(parsedLine.isNationalRail());
		assertFalse(parsedLine.isTube());
		
		parsedLine = parser.parseRouteStopLine(LINE_WITH_NATIONAL_RAIL_AND_UNDERGROUND_CHANGES);
		assertTrue(parsedLine.isNationalRail());
		assertTrue(parsedLine.isTube());
		
		parsedLine = parser.parseRouteStopLine(LINE_WITH_NATIONAL_RAIL_ONLY);
		assertTrue(parsedLine.isNationalRail());
		assertFalse(parsedLine.isTube());		
		
		parsedLine = parser.parseRouteStopLine(LINE_WITH_TUBE_ONLY);
		assertFalse(parsedLine.isNationalRail());
		assertTrue(parsedLine.isTube());
	}
	
	@Test
	public void shouldCleanStationHintsFromStopNamess() throws Exception {
		RouteStop parsedLine = parser.parseRouteStopLine(LINE_WITH_NATIONAL_RAIL_AND_UNDERGROUND_CHANGES);
		assertEquals("RICHMOND STATION", parsedLine.getStop_Name());
	}
	
	@Test
	public void shouldParseVirtualStopsCorrectly() throws Exception {		
		RouteStop parsedLine = parser.parseRouteStopLine(VIRTUAL_STOP);
		assertTrue(parsedLine.getVirtual_Bus_Stop());
	}
	
}
