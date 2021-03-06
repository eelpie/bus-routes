package uk.co.eelpieconsulting.busroutes.services.caching;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MemcachedCache {
	
	private static Logger log = Logger.getLogger(MemcachedCache.class);
		
	private MemcachedClient memcachedClient;

	@Value("#{busRoutes['memcachedUrls']}")
	private String memcachedUrls;
	
	public void put(String key, int ttl, Object value) {
		try {
			memcachedClient = getClient();
			memcachedClient.set(key, ttl, value);
			
		} catch (IOException e) {
			log.warn("Failed to put to cache: " + e.getMessage());
		}
	}

	public Object get(String key) {
		MemcachedClient memcachedClient;
		try {
			memcachedClient = getClient();
			return memcachedClient.get(key);
		} catch (IOException e) {
			log.warn("Failed to get from cache: " + e.getMessage());
		}
		return null;
	}
	
	public void decache(String key) {
		try {
			memcachedClient = getClient();
			memcachedClient.delete(key);
		} catch (IOException e) {
			log.error("Failed to decache from cache: " + e.getMessage());
		}		
	}
	
	private MemcachedClient getClient() throws IOException {
		if (memcachedClient == null) {
			memcachedClient= new MemcachedClient(AddrUtil.getAddresses(memcachedUrls));
		}
		return memcachedClient;
	}
	
}
