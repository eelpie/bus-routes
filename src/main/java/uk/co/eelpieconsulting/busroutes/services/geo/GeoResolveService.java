package uk.co.eelpieconsulting.busroutes.services.geo;

import java.io.IOException;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.NominatimClient;
import fr.dudie.nominatim.model.Address;
import fr.dudie.nominatim.model.AddressElement;

@Component
public class GeoResolveService {
	
	private static Logger log = Logger.getLogger(GeoResolveService.class);

	private static final String NOMINATIM_USERNAME = "tony@eelpieconsulting.co.uk";
	private static final String NOMINATIM_URL = "http://nominatim.openstreetmap.org/";
	
	private static final int RESOLVER_ZOOM_LEVEL = 17;
	
	private Joiner commaJoiner = Joiner.on(", ").skipNulls();
	
	public String resolvePointName(double latitude, double longitude) {
		final NominatimClient nominatimClient = new JsonNominatimClient(NOMINATIM_URL, new DefaultHttpClient(), NOMINATIM_USERNAME, null, false, false);
		try {
			return buildDisplayName(nominatimClient.getAddress(longitude, latitude, RESOLVER_ZOOM_LEVEL));
		} catch (IOException e) {
			log.error("Error while resolving location", e);
			throw new RuntimeException(e);
		}
	}

	private String buildDisplayName(final Address address) {
		boolean finished = false;		
		final List<String> addressComponentsToDisplay = Lists.newArrayList();
		for (AddressElement addressElement : address.getAddressElements()) {			
			if (!finished) {
				addressComponentsToDisplay.add(addressElement.getValue());
			}
			if (addressElement.getKey().equals("city")) {
				finished = true;
			}
		}	
		return commaJoiner.join(addressComponentsToDisplay);
	}
	
}
