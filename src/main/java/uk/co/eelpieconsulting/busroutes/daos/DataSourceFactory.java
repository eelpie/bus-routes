package uk.co.eelpieconsulting.busroutes.daos;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import uk.co.eelpieconsulting.busroutes.model.RouteStop;

import com.google.common.collect.Lists;

@Component
public class DataSourceFactory {
	
    @Value("#{busRoutes['mongoHost']}")
    private String mongoHost;
    
    @Value("#{busRoutes['mongoDatabase']}")
    private String mongoDatabase;
    
    @Value("#{busRoutes['mongoUser']}")
    private String mongoUser;
    
    @Value("#{busRoutes['mongoPassword']}")
    private String mongoPassword;

    @Value("#{busRoutes['mongoSSL']}")
    private Boolean mongoSSL;

	public DataSourceFactory() {
	}

	public DataSourceFactory(String mongoHost, String mongoDatabase) {
		this.mongoHost = mongoHost;
		this.mongoDatabase = mongoDatabase;
	}
	
	public Datastore getDatastore() throws UnknownHostException, MongoException {	
		Morphia morphia = new Morphia();
		
        MongoCredential credential = MongoCredential.createCredential(mongoUser, mongoDatabase, mongoPassword.toCharArray());
        List credentials = Lists.newArrayList(credential);

        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().sslEnabled(mongoSSL).build();
		
        MongoClient m = new MongoClient(new ServerAddress(mongoHost), credentials, mongoClientOptions);
        final Datastore dataStore =  morphia.createDatastore(m, mongoDatabase);        
		morphia.map(RouteStop.class);
		dataStore.ensureIndexes();
		return dataStore;
	}
	
}
