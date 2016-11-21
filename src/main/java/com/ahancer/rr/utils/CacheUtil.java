package com.ahancer.rr.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

@Component
public class CacheUtil {
	@Autowired
	private org.springframework.cache.CacheManager cacheManager;
	public Object getCacheObject(String cacheName,Object key){
		Cache cache = cacheManager.getCache(cacheName);
		ValueWrapper wrapper = cache.get(key);
		if(null != wrapper) {
			return wrapper.get();
		} else {
			return null;
		}
	}
	public void putCacheObject(String cacheName,Object key,Object value) {
		Cache cache = cacheManager.getCache(cacheName);
		cache.put(key, value);
	}
	public void removeCacheObject(String cacheName,Object key) {
		Cache cache = cacheManager.getCache(cacheName);
		if(null != cache){
			cache.putIfAbsent(key, null);
		}
	}
	public void updateCacheObject(String cacheName,Object key,Object newValue) {
		removeCacheObject(cacheName,key);
		putCacheObject(cacheName,key,newValue);
	}

}
