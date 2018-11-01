package com.ultrapower.sbdemo.common.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

import com.ultrapower.sbdemo.common.LogManage;

import org.ehcache.Cache;
import org.ehcache.Cache.Entry;

/**
 * 
 * @ClassName:     EhCacheUtil
 * @Description:   ehcache操作类
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月26日 上午9:39:44 
 *
 */
public class EhCacheUtil {
	/**
	 * 公用缓存名称
	 */
	public static final String CACHENAME_COMMON = "web_cacheName_common";

	 
	/**
	 * ehcache缓存管理
	 */
	private static CacheManager cacheManager = null;

	/**
	 * 添加缓存
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public static void put(String cacheName, String key, Object value) {
		try{
			Cache<String, Object> cache = getCache(cacheName); 
		    if(null!=cache){
	            synchronized(cache) {
	            	cache.put(key, value);
	            }
	        }
		}catch(Exception ex){
			LogManage.setLogErrorInfor(EhCacheUtil.class, cacheName + " put key:"+key+",value:"+value+" error! ", ex);
			ex.printStackTrace();
		} 
	}  

	/**
	 * 获取缓存
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static Object get(String cacheName, String key) { 
		try{
			Cache<String, Object> cache = getCache(cacheName); 
			Object e = null;
			if(null!=cache){
				synchronized(cache) {  
					e = cache.get(key);  
		        }
			}
			return e;
		}catch(Exception ex){
			LogManage.setLogErrorInfor(EhCacheUtil.class, cacheName + " get key:"+key+" error! ", ex);
			ex.printStackTrace();
		}
		return null;
	}  

	/**
	 * 获取缓存名 数组
	 * @return
	 */
	public static String[] getCacheNames() {  
//		for (String cacheName : cacheManager.getCacheNames()) {
//		    System.out.println(cacheName);
//		}
		return null;
	} 
	
	/**
	 * 获取缓存keys
	 * @param cacheName
	 * @return
	 */
	public static List<String> getKeys(String cacheName) { 
		List<String> result = new ArrayList<String>();
		try{
			Cache<String, Object> cache = getCache(cacheName); 
			if(null!=cache){
				synchronized(cache) {  
					Iterator<Entry<String, Object>> it = cache.iterator();
	                while(it.hasNext()){
	                	Entry<String, Object> ent = it.next();
	                	if(null!=ent && null!=ent.getKey()) {
	                		String key = ent.getKey();
	                		result.add(key);
	                	}
	                } 
		        }
			}
			return result;
		}catch(Exception ex){
			LogManage.setLogErrorInfor(EhCacheUtil.class, cacheName + " get keys error! ", ex);
			ex.printStackTrace();
		}
		return null;  
	} 
	
	
	/**
	 * 获取缓存对象数量
	 * @param cacheName
	 * @return
	 */
	public static int getCacheKeySize(String cacheName) {  
	    int cont = 0;
	    try {
	    	 List<String> list = getKeys(cacheName);
	    	 if(null!=list) {
	    		 cont = list.size();
	    	 }
	    }catch(Exception ex) {
			LogManage.setLogErrorInfor(EhCacheUtil.class, cacheName + " getSize error! ", ex);
			ex.printStackTrace();
	    }
	    return cont;
	}  
	
	
	/**
	 * 清除所有缓存
	 */
	public static void clearAll() {  
		//getCacheManager().clearAll();  
	} 
	
	/**
	 * 清空指定缓存
	 * @param cacheName
	 */
	public static void clear(String cacheName) {
		synchronized(getCacheManager()) {  
			getCacheManager().removeCache(cacheName);
        } 
	}  

	
	/**
	 * 删除指定对象
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static boolean remove(String cacheName, String key) {  
		try{
			Cache<String, Object> cache = getCache(cacheName);  
		    if(null!=cache){
	            synchronized(cache) {
	            	if(cache.containsKey(key)){
	            		cache.remove(key);
	            	}
	            }
	        }
			return true;
		}catch(Exception ex){
			LogManage.setLogErrorInfor(EhCacheUtil.class, cacheName + " catch remove "+key+" error! ", ex);
			return false; 
		} 
	} 
	
	
	
	

	
	/**
	 * 获取Cache
	 * 
	 * @Description: TODO (这里用一句话描述这个方法的作用) 
	 * @param cacheName
	 * @return
	 * @author caijinpeng
	 */
	private static Cache<String, Object> getCache(String cacheName){
		try{
			Cache<String, Object> myCache = getCacheManager().getCache(cacheName, 
					String.class, Object.class); 
			
			if(null==myCache){
				myCache = getCacheManager().createCache(cacheName,  
			             CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class,ResourcePoolsBuilder.heap(100))
			             .withExpiry(Expirations.timeToLiveExpiration(Duration.of(12, TimeUnit.HOURS)))
			             .build());  
			}
			return myCache;
		}catch(Exception ex){
			LogManage.setLogErrorInfor(EhCacheUtil.class, "get Cache error! ", ex);
			ex.printStackTrace();
		}
		return null;  
	}
	
	/**
	 * 获取CacheManager
	 * 
	 * @return
	 */
	private static CacheManager getCacheManager() {
		if (cacheManager != null) {
			return cacheManager;
		}
		try {			
			cacheManager = CacheManagerBuilder.newCacheManagerBuilder() 
					.withCache("preConfigured",
				             CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class,ResourcePoolsBuilder.heap(100))
				             .withExpiry(Expirations.timeToLiveExpiration(Duration.of(12, TimeUnit.HOURS)))
				             .build())
					.build(); 
			cacheManager.init();

		} catch (RuntimeException ex) {
			LogManage.setLogErrorInfor(EhCacheUtil.class, "create CacheManager error! ", ex);
			ex.printStackTrace();
		}
		return cacheManager;
	}

	
	
	// 测试
	public static void main(String[] args) {
		put(CACHENAME_COMMON,"key1","hello world1");
		put(CACHENAME_COMMON,"key2","hello world2");
		Object key1Value = get(CACHENAME_COMMON,"key1");
		System.out.println("key1Value===1="+key1Value);
		Object key2Value = get(CACHENAME_COMMON,"key1");
		System.out.println("key1Value===1="+key2Value);
		
		remove(CACHENAME_COMMON,"key1");
		Object key1Value2 = get(CACHENAME_COMMON,"key1");
		System.out.println("key1Value===2="+key1Value2);

		Object key2Value2 = get(CACHENAME_COMMON,"key2");
		System.out.println("key1Value===2="+key2Value2);
		
		List<String> list = getKeys(CACHENAME_COMMON);
		for(int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}
		
	}
}
