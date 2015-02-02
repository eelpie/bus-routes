package uk.co.eelpieconsulting.busroutes.model;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("routesfilechecksum")
public class RoutesFileChecksum {

	@Id
	ObjectId objectId;
	
	private String md5;
	
	public RoutesFileChecksum() {
	}

	public RoutesFileChecksum(String md5) {
		this.md5 = md5;
	}
	
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}

	@Override
	public String toString() {
		return "RoutesFileChecksum [md5=" + md5 + "]";
	}
	
}
