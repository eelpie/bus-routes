package uk.co.eelpieconsulting.busroutes.parsing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import uk.co.eelpieconsulting.busroutes.geo.OSRefConvertor;
import uk.co.eelpieconsulting.busroutes.model.RouteStop;
import uk.me.jstott.jcoord.LatLng;

public class RoutesParser {
	
	private static final String UTF_8 = "UTF-8";
	
	private final OSRefConvertor osRefConvertor;
	
	public RoutesParser(OSRefConvertor osRefConvertor) {
		this.osRefConvertor = osRefConvertor;
	}
	
	public List<RouteStop> parseRoutesFile(InputStream input) {
		final List<RouteStop> routeStops = new ArrayList<RouteStop>();
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(input, UTF_8));			

			reader.readLine();
			while (reader.ready()) {			
				final String line = reader.readLine();
				try {
					routeStops.add(parseRouteStopLine(line));
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

	private RouteStop parseRouteStopLine(String line) {
		try {
			final String[] fields = line.split(",");

			final String routeName = fields[0];			
			final int stopId = Integer.parseInt(fields[4]);
			final int run = Integer.parseInt(fields[1]);
			final String stopName = fields[6];
			final boolean virtualStop = fields[9].equals("1") ? true : false;
			
			final int easting = Integer.parseInt(fields[7]);
			final int northing = Integer.parseInt(fields[8]);
			
			final LatLng latLng = osRefConvertor.toLatLng(easting, northing);
			
			final int sequenceNumber = Integer.parseInt(fields[2]);
			return new RouteStop(stopId, run, virtualStop, sequenceNumber, routeName, stopName, new double[]{latLng.getLatitude(), latLng.getLongitude()});
			
		} catch (Exception e) {
			throw new RuntimeException("Unparseable line: " + line);
		}
	}

}
