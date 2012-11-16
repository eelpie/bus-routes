package uk.co.eelpieconsulting.busroutes.services.geo;

import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.common.geo.LatLong;
import uk.co.eelpieconsulting.common.geo.OSRefConvertor;

@Component
public class EastingsNorthingsConvertor {

	private OSRefConvertor osRefConvertor;
	
	public EastingsNorthingsConvertor() {
		osRefConvertor = new OSRefConvertor();
	}

	public LatLong toLatLong(int easting, int northing) {
		return osRefConvertor.toLatLong(easting, northing);
	}
	
}
