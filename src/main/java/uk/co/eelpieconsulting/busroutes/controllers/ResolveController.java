package uk.co.eelpieconsulting.busroutes.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uk.co.eelpieconsulting.common.views.ViewFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.NominatimClient;
import fr.dudie.nominatim.model.Address;
import fr.dudie.nominatim.model.AddressElement;

@Controller
public class ResolveController {
	
	private static final String NOMINATIM_USERNAME = "tony@eelpieconsulting.co.uk";
	private static final String NOMINATIM_URL = "http://nominatim.openstreetmap.org/";
	
	private static final int RESOLVER_ZOOM_LEVEL = 17;
	private static final int ONE_MINUTE = 60;
	private static final int ONE_HOUR = 60 * ONE_MINUTE;
	
	private final ViewFactory viewFactory;
	
	private Joiner commaJoiner = Joiner.on(", ").skipNulls();
	
	@Autowired
	public ResolveController(ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}
	
	@RequestMapping("/resolve")
	public ModelAndView resolve(@RequestParam(value="latitude", required=true) double latitude, 
			@RequestParam(value="longitude", required=true) double longitude, @RequestParam(value="resolve", required=false) Boolean resolve) throws IOException {		
		final Address address = resolvePointName(latitude, longitude);
		
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView(ONE_HOUR));
		mv.addObject("data",  address != null ? buildDisplayName(address) : null);
		return mv;
	}
	
	private Address resolvePointName(double latitude, double longitude) throws IOException {
		final NominatimClient nominatimClient = new JsonNominatimClient(NOMINATIM_URL, new DefaultHttpClient(), NOMINATIM_USERNAME, null, false, false);
		return nominatimClient.getAddress(longitude, latitude, RESOLVER_ZOOM_LEVEL);
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
