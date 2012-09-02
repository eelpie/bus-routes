package uk.co.eelpieconsulting.busroutes.model;

import java.util.Date;

public class FileInformation {

	private final String name;
	private final Date date;
	private final String md5;

	public FileInformation(String name, Date date, String md5) {
		this.name = name;
		this.date = date;
		this.md5 = md5;
	}

	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}

	public String getMd5() {
		return md5;
	}

	@Override
	public String toString() {
		return "FileInformation [date=" + date + ", md5=" + md5 + ", name=" + name + "]";
	}
	
}
