package uk.co.eelpieconsulting.busroutes.model;

public class Route {

	private final String route;
	private final int run;
	private final String towards;

	public Route(String route, int run, String towards) {
		this.route = route;
		this.run = run;		
		this.towards = towards;
	}

	public String getRoute() {
		return route;
	}

	public int getRun() {
		return run;
	}
	
	public String getTowards() {
		return towards;
	}
	
	@Override
	public String toString() {
		return "Route [route=" + route + ", run=" + run + ", towards=" + towards + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((route == null) ? 0 : route.hashCode());
		result = prime * result + run;
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
		Route other = (Route) obj;
		if (route == null) {
			if (other.route != null)
				return false;
		} else if (!route.equals(other.route))
			return false;
		if (run != other.run)
			return false;
		return true;
	}
	
}
