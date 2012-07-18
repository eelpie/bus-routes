package  uk.co.eelpieconsulting.busroutes.views;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class EtagGenerator {

	public String makeEtagFor(String data) {
		return DigestUtils.md5Hex(data);
	}
	
}
