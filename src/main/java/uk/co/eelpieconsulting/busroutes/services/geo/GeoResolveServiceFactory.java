package uk.co.eelpieconsulting.busroutes.services.geo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.common.geo.GeoCodingService;
import uk.co.eelpieconsulting.common.geo.NominatimGeocodingService;

@Component
public class GeoResolveServiceFactory {

	private final String nominatimEmail;
	private final String nominatimUrl;
	
	@Autowired
	public GeoResolveServiceFactory( @Value("#{busRoutes['nominatimEmail']}")String nominatimEmail, @Value("#{busRoutes['nominatimUrl']}") String nominatimUrl) {
		this.nominatimEmail = nominatimEmail;
		this.nominatimUrl = nominatimUrl;
	}
	
	public GeoCodingService getGeoResolveService() {
		return new NominatimGeocodingService(nominatimEmail, nominatimUrl);
	}
	
}
