package uk.co.eelpieconsulting.busroutes.daos;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.RoutesFileChecksum;

import com.google.code.morphia.Datastore;
import com.mongodb.MongoException;

@Component
public class RoutesFileChecksumDAO {
			
	private final Datastore datastore;

	@Autowired
	public RoutesFileChecksumDAO(DataSourceFactory dataSourceFactory) throws UnknownHostException, MongoException {
		this.datastore = dataSourceFactory.getDatastore();
	}
	
	public String getChecksum() {
		final RoutesFileChecksum checksumObject = getChecksumObject();
		if (checksumObject != null) {
			return checksumObject.getMd5();
		}
		return null;
	}

	private synchronized RoutesFileChecksum getChecksumObject() {
		final List<RoutesFileChecksum> checksums = datastore.createQuery(RoutesFileChecksum.class).asList();
		if (checksums.isEmpty()) {
			return null;
		}
		return checksums.get(0);
	}

	public synchronized void setChecksum(String md5) {
		RoutesFileChecksum checksum = getChecksumObject();
		if (checksum == null) {
			checksum = new RoutesFileChecksum(md5);
		}
		datastore.save(checksum);
	}
	
}
