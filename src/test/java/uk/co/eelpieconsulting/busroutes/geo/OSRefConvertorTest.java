package uk.co.eelpieconsulting.busroutes.geo;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.me.jstott.jcoord.LatLng;

public class OSRefConvertorTest {

	@Test
	public void canConvertEastingNorthingToLatLng() throws Exception {		
		final OSRefConvertor convertor = new OSRefConvertor();
		
		final LatLng latLng = convertor.toLatLng(516346, 173388);
		
		assertEquals(51.4475, latLng.getLat(), 0.01);	// TODO more accuracy
		assertEquals(-0.3271, latLng.getLng(), 0.01);
	}
	
}
