/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gopivotal.jp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.query.FunctionDomainException;
import com.gemstone.gemfire.cache.query.NameResolutionException;
import com.gemstone.gemfire.cache.query.Query;
import com.gemstone.gemfire.cache.query.QueryInvocationTargetException;
import com.gemstone.gemfire.cache.query.QueryService;
import com.gemstone.gemfire.cache.query.TypeMismatchException;
import com.gemstone.org.jgroups.ExitEvent;

public class Main {
	static Log log = LogFactory.getLog(Main.class);
	/**
	 * @param args
	 * @throws QueryInvocationTargetException 
	 * @throws NameResolutionException 
	 * @throws TypeMismatchException 
	 * @throws FunctionDomainException 
	 */
	public static void main(String[] args) throws FunctionDomainException, TypeMismatchException, NameResolutionException, QueryInvocationTargetException {
//		ApplicationContext context= new ClassPathXmlApplicationContext("META-INF/spring/gemfire/cache-config.xml");
		ApplicationContext context= new ClassPathXmlApplicationContext("META-INF/spring/gemfire/cache-config.xml");
		@SuppressWarnings("unchecked")
//		Region<Object,Object> region = context.getBean("myRegion",Region.class);
		
//		Region<Object, ArrayList<String>> region = context.getBean("myRegion", Region.class);

		
		
		Region<Object, HashMap<Object, Object>> region = context.getBean("myRegion", Region.class);
		
		log.debug("populating region ...");

		Map map = new HashMap<Object, Object>();
	    map.put("binkey1", "binvalue1");
	    map.put("binkey2", "binvalue2");
	    
		region.put(1, (HashMap<Object, Object>) map);
		
//		region.put(1, (ArrayList<String>) Arrays.asList(new String[]{"One", "Two", "Three"}));
//		region.put(2,"banana");
//		region.put(3,"pear");
		
		Cache cache = region.getCache();
	    QueryService queryService = cache.getQueryService();
	    Query query = null;
	    Object result = null;
	    query = queryService.newQuery("SELECT values.keySet() FROM /myRegion.values values");
	    System.out.println("\nExecuting query:\n\t" + query.getQueryString());
	    result = query.execute();
	    System.out.println("Query result:\n\t" + result);
	    query = queryService.newQuery("SELECT * FROM /myRegion.entrySet ");
	    System.out.println("\nExecuting query:\n\t" + query.getQueryString());
	    result = query.execute();
	    System.out.println("Query result:\n\t" + result);
	    		
		System.out.println("HashKey names are: " + region.get(1).keySet());
		System.out.println("HashKey names are: " + region.getName());
		System.out.println("This is for binkey1: " + region.get(1).get("binkey1"));
		System.out.println("This is for binkey2: " + region.get(1).get("binkey2"));
	
		log.debug("retreiving region values...");

		for (Object obj:region.values()) {
		log.debug(obj);
		}

		log.debug("retreiving region keys...");
		for (Object obj:region.keySet()) {
		log.debug(obj+":" + region.get(obj));
		}
	
	}
}
