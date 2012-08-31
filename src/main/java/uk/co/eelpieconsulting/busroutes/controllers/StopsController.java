package uk.co.eelpieconsulting.busroutes.controllers;

import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uk.co.eelpieconsulting.busroutes.model.MultiStopMessage;
import uk.co.eelpieconsulting.busroutes.parsing.CountdownService;
import uk.co.eelpieconsulting.busroutes.services.MessageService;
import uk.co.eelpieconsulting.busroutes.services.StopsService;
import uk.co.eelpieconsulting.common.http.HttpFetchException;
import uk.co.eelpieconsulting.common.views.ViewFactory;
import uk.co.eelpieconsulting.countdown.exceptions.ParsingException;
import uk.co.eelpieconsulting.countdown.model.StopBoard;

@Controller
public class StopsController {
	
	private static final int TEN_SECONDS = 10;	
	private static final int ONE_MINUTE = 60;
	private static final int TEN_MINUTES = 10 * ONE_MINUTE;
	private static final int ONE_HOUR = 60 * ONE_MINUTE;
	
	private final StopsService stopsService;
	private final CountdownService countdownService;
	private final MessageService messageService;
	private final ViewFactory viewFactory;
	
	@Autowired
	public StopsController(StopsService stopsService, CountdownService countdownService, MessageService messageService, ViewFactory viewFactory) {
		this.stopsService = stopsService;
		this.countdownService = countdownService;
		this.messageService = messageService;
		this.viewFactory = viewFactory;
	}
	
	@RequestMapping("/stop/{id}")
	public ModelAndView stop(@PathVariable int id) {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView(ONE_HOUR));
		mv.addObject("data", stopsService.getStopById(id));
		return mv;
	}
	
	@RequestMapping("/stop/{id}/arrivals")
	public ModelAndView arrivals(@PathVariable int id) throws HttpFetchException, ParsingException {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView(TEN_SECONDS));		
		final StopBoard stopBoard = countdownService.getStopBoard(id);
		mv.addObject("data", stopBoard);
		return mv;
	}
	
	@RequestMapping("/stop/{id}/messages")
	public ModelAndView messages(@PathVariable int id) throws HttpFetchException, ParsingException {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView(TEN_MINUTES));		
		mv.addObject("data", messageService.getMessages(id));
		return mv;
	}
	
	@RequestMapping("/messages")
	public ModelAndView multiMessages(@RequestParam(value="stops", required = false) int[] stops) throws HttpFetchException, ParsingException {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView(TEN_MINUTES));
		final List<MultiStopMessage> messages = messageService.getMessages(stops);		
		mv.addObject("data", messages);
		return mv;
	}                                       
	
	@RequestMapping("/stops/near")
	public ModelAndView stopsNear(@RequestParam(value="latitude", required=true) double latitude, 
			@RequestParam(value="longitude", required=true) double longitude) {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView(ONE_HOUR));
		mv.addObject("data", stopsService.findStopsNear(latitude, longitude));
		return mv;
	}
	
	@RequestMapping("/routes/near")
	public ModelAndView routesNear(@RequestParam(value="latitude", required=true) double latitude, 
			@RequestParam(value="longitude", required=true) double longitude) {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView(ONE_HOUR));
		mv.addObject("data", stopsService.findRoutesNear(latitude, longitude));
		return mv;
	}
	
	@RequestMapping("/stops/search")
	public ModelAndView search(@RequestParam(value="q", required=true) String q) throws SolrServerException {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView(ONE_HOUR));	
		mv.addObject("data",  stopsService.search(q));
		return mv;
	}
	
	@RequestMapping("/route/{route}/{run}/stops")
	public ModelAndView route(@PathVariable String route, @PathVariable int run) {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView(ONE_HOUR));				
		mv.addObject("data", stopsService.findStopsForRoute(route, run));				
		return mv;
	}
	
}
