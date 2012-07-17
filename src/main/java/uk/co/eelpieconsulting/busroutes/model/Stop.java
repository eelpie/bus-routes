package uk.co.eelpieconsulting.busroutes.model;

import java.util.HashSet;
import java.util.Set;

public class Stop {

	private final int id;
	private final String name;
	private final double latitude;
	private final double longitude;
	private final Set<Route> routes;

	public Stop(int id, String name, double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.routes = new HashSet<Route>();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public void addRoute(Route route) {
		routes.add(route);
	}
	
	public Set<Route> getRoutes() {
		return routes;
	}

	@Override
	public String toString() {
		return "Stop [id=" + id + ", latitude=" + latitude + ", longitude="
				+ longitude + ", name=" + name + ", routes=" + routes + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stop other = (Stop) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
