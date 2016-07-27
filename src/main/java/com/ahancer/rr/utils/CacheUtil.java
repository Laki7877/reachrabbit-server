package com.ahancer.rr.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheUtil {
	
	public static Object getCacheObject(String cacheName,Object key){
		CacheManager manager = CacheManager.getInstance();
		Cache cache = manager.getCache(cacheName);
		Element element = cache.get(key);
		if(null != element) {
			return element.getObjectValue();
		}else {
			return null;
		}
	}
	
	
	public static void putCacheObject(String cacheName,Object key,Object value){
		CacheManager manager = CacheManager.getInstance();
		Cache cache = manager.getCache(cacheName);
		Element element = new Element(key, value);
		cache.put(element);
	}
	

}
