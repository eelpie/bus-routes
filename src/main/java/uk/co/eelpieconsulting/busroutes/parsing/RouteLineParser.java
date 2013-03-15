package uk.co.eelpieconsulting.busroutes.parsing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.RouteStop;
import uk.co.eelpieconsulting.busroutes.services.geo.EastingsNorthingsConvertor;
import uk.co.eelpieconsulting.common.geo.model.LatLong;

@Component
public class RouteLineParser {
	
	private static final String TUBE_STATION = "<>";
	private static final String NATIONAL_RAIL_STATION = "#";
	private static final String TRAM_STATION = "<T>";
	
	private static final int SEQUENCE = 2;
	private static final int NORTHING = 8;
	private static final int EASTING = 7;
	private static final int VIRTUAL_STOP = 10;
	private static final int STOP_NAME = 6;
	private static final int RUN = 1;
	private static final int STOP_ID = 4;
	private static final int ROUTE_NAME = 0;
	private final EastingsNorthingsConvertor eastingsNorthingsConvertor;
	
	@Autowired
	public RouteLineParser(EastingsNorthingsConvertor eastingsNorthingsConvertor) {
		this.eastingsNorthingsConvertor = eastingsNorthingsConvertor;
	}
	
	public RouteStop parseRouteStopLine(String line) {
		try {
			final String[] fields = line.split(",");

			final String routeName = fields[ROUTE_NAME];			
			final int stopId = Integer.parseInt(fields[STOP_ID]);
			final int run = Integer.parseInt(fields[RUN]);
			final boolean virtualStop = fields[VIRTUAL_STOP].equals("1") ? true : false;
			final int sequenceNumber = Integer.parseInt(fields[SEQUENCE]);
			
			final int easting = Integer.parseInt(fields[EASTING]);
			final int northing = Integer.parseInt(fields[NORTHING]);			
			final LatLong latLong = eastingsNorthingsConvertor.toLatLong(easting, northing);
						
			String stopName = fields[STOP_NAME];
			final boolean nationalRail = stopName.contains(NATIONAL_RAIL_STATION);
			final boolean tube = stopName.contains(TUBE_STATION);
			final boolean tram = stopName.contains(TRAM_STATION);
			
			stopName = stopName.replace(NATIONAL_RAIL_STATION, "").trim();
			stopName = stopName.replace(TUBE_STATION, "").trim();
			stopName = stopName.replace(TRAM_STATION, "").trim();
			
			return new RouteStop(stopId, run, virtualStop, sequenceNumber, routeName, stopName, new double[]{latLong.getLatitude(), latLong.getLongitude()}, nationalRail, tube, tram);
			
		} catch (Exception e) {
			throw new RuntimeException("Unparseable line: " + line);
		}
	}

}
