package uk.co.eelpieconsulting.busroutes.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

import uk.co.eelpieconsulting.busroutes.views.json.JsonSerializer;
import uk.co.eelpieconsulting.busroutes.views.json.JsonView;

@Component
public class ViewFactory {

	private final EtagGenerator etagGenerator;
	
	@Autowired
	public ViewFactory(EtagGenerator etagGenerator) {
		this.etagGenerator = etagGenerator;
	}

	public View getJsonView() {
		return new JsonView(new JsonSerializer(), etagGenerator);
	}

}
