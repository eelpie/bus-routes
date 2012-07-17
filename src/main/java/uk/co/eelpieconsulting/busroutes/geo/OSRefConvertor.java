package uk.co.eelpieconsulting.busroutes.geo;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.OSRef;

public class OSRefConvertor {
	
	public LatLng toLatLng(double easting, double northing) {
		final OSRef ref = new OSRef(easting, northing);
		return ref.toLatLng();
	}

}
