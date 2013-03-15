package uk.co.eelpieconsulting.busroutes.services.geo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.common.geo.GeoCodingService;
import uk.co.eelpieconsulting.common.geo.NominatimGeocodingService;

@Component
public class GeoResolveServiceFactory {

    @Value("#{busRoutes['nominatimEmail']}")
	private String nominatimEmail;
    @Value("#{busRoutes['nominatimUrl']}")
	private String nominatimUrl;
    
	public GeoCodingService getGeoResolveService() {
		return new NominatimGeocodingService(nominatimEmail, nominatimUrl);
	}
	
}
