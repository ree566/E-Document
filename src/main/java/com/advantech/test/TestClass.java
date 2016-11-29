/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import static java.lang.System.out;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String arg0[]) {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        cacheManager.init();
        out.println(cacheManager.getStatus());
//        Cache<Long, String> myCache = cacheManager.createCache("myCache",
//                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10)).build());
//        Cache<Long, String> myCache = cacheManager.getCache("myCache", Long.class, String.class);
//        myCache.put(1L, "da one!");
//        String value = myCache.get(1L);
//        out.println(value);
        
//        cacheManager.close();
    }
}
