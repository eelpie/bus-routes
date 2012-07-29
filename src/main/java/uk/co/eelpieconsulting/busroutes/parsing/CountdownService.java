package uk.co.eelpieconsulting.busroutes.parsing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.Message;
import uk.co.eelpieconsulting.countdown.api.CountdownApi;
import uk.co.eelpieconsulting.countdown.exceptions.HttpFetchException;
import uk.co.eelpieconsulting.countdown.exceptions.ParsingException;
import uk.co.eelpieconsulting.countdown.model.StopBoard;

@Component
public class CountdownService {
	
	private CountdownApi countdownApi;

	public CountdownService() {
		countdownApi = new CountdownApi("http://countdown.api.tfl.gov.uk");	// TODO inject
	}
	
	public StopBoard getStopBoard(int stopId) {
		try {
			return countdownApi.getStopBoard(stopId);
		} catch (HttpFetchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}
	
	public List<Message> getStopMessages(int stopId) {
		try {
			return countdownApi.getStopMessages(stopId);
		} catch (HttpFetchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<Message> getMultipleStopMessages(int[] stopIds) {
		final List<Message> messages = new ArrayList<Message>();
		for (int stopId : stopIds) {
			messages.addAll(getStopMessages(stopId));
		}
		return messages;
	}
	
}
