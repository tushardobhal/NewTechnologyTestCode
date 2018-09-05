package com.test.hazelcast;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import com.test.dao.Name;

/*
	 * Test program for Distributed Executor Service using Hazelcast
	 */
	public class DistributedTask implements Callable<Boolean>, Serializable {
		private static final long serialVersionUID = 1L;
		private static final Logger LOGGER = LoggerFactory.getLogger(DistributedTask.class);
		
		@Autowired
		@Qualifier("hazelcastInstance")
		private HazelcastInstance hzInstance;
		
		private String key;
		
		@Override
		public Boolean call() throws Exception {
			IMap<String, Name> hzMap = hzInstance.getMap("namesMap");
			ILock lock = hzInstance.getLock(key);
			lock.lock();
			boolean result = false;
			if(!hzMap.containsKey(key)) {
				LOGGER.info("{} key not present in map", key);
			} else {
				result = true;
				LOGGER.info("Name {} present in map", hzMap.get(key));
			}
			lock.unlock();
			return result;
		}

		public DistributedTask setKey(String key) {
			this.key = key;
			return this;
		}
		
	}