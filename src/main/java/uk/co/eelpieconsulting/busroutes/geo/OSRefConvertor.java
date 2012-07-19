package uk.co.eelpieconsulting.busroutes.geo;

import org.springframework.stereotype.Component;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.OSRef;

@Component
public class OSRefConvertor {
	
	public LatLng toLatLng(double easting, double northing) {
		final OSRef ref = new OSRef(easting, northing);
		LatLng latLng = ref.toLatLng();
		latLng.toWGS84();
		return latLng;
	}

}