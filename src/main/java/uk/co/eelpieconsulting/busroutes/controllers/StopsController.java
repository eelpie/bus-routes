package uk.co.eelpieconsulting.busroutes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uk.co.eelpieconsulting.busroutes.services.StopsService;
import uk.co.eelpieconsulting.busroutes.views.ViewFactory;
import uk.co.eelpieconsulting.countdown.exceptions.HttpFetchException;
import uk.co.eelpieconsulting.countdown.exceptions.ParsingException;
import uk.co.eelpieconsulting.countdown.model.StopBoard;

@Controller
public class StopsController {
	
	private final StopsService stopsService;
	private final ViewFactory viewFactory;
	
	@Autowired
	public StopsController(StopsService stopsService, ViewFactory viewFactory) {
		this.stopsService = stopsService;
		this.viewFactory = viewFactory;
	}
	
	@RequestMapping("/stop/{id}")
	public ModelAndView stop(@PathVariable int id) {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView());
		mv.addObject("data", stopsService.getStopById(id));
		return mv;
	}
	
	@RequestMapping("/stop/{id}/arrivals")
	public ModelAndView arrivals(@PathVariable int id) throws HttpFetchException, ParsingException {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView());		
		final StopBoard stopBoard = stopsService.getStopBoard(id);
		mv.addObject("data", stopBoard);
		return mv;
	}
	
	@RequestMapping("/stops/near")
	public ModelAndView stopsNear(@RequestParam(value="latitude", required=true) double latitude, 
			@RequestParam(value="longitude", required=true) double longitude) {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView());
		mv.addObject("data", stopsService.findStopsNear(latitude, longitude));
		return mv;
	}
	
	@RequestMapping("/route/{route}/{run}/stops")
	public ModelAndView route(@PathVariable String route, @PathVariable int run) {
		final ModelAndView mv = new ModelAndView(viewFactory.getJsonView());				
		mv.addObject("data", stopsService.findStopsForRoute(route, run));				
		return mv;
	}
	
}
