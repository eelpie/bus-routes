package uk.co.eelpieconsulting.busroutes.views.json;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import uk.co.eelpieconsulting.busroutes.views.EtagGenerator;

public class JsonView implements View {
	
	private final JsonSerializer jsonSerializer;
	private final EtagGenerator etagGenerator;
	private Integer maxAge;

	public JsonView(JsonSerializer jsonSerializer, EtagGenerator etagGenerator) {
		this.jsonSerializer = jsonSerializer;
		this.etagGenerator = etagGenerator;
	}
	
	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	@Override
	public String getContentType() {
		return "application/json";
	}

	@Override
	public void render(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(getContentType());
    	response.setHeader("Cache-Control", "max-age=" + (maxAge != null ? maxAge : 0));		
		
		final String json = jsonSerializer.serialize(model.get("data"));
		response.setHeader("Etag", etagGenerator.makeEtagFor(json));
		
		String callbackFunction = null;
		if (model.containsKey("callback")) {			
			callbackFunction = (String) model.get("callback");
			response.getWriter().write(callbackFunction + "(");			
		}
		
		response.getWriter().write(json);
		
		if (callbackFunction != null) {
			response.getWriter().write(");");			
		}
		
		response.getWriter().flush();
	}

}
