package com.test.hazelcast;

import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import com.test.dao.Name;
/*
 * Test program which defines a distributed hazelcast map.
 * It updates the age if the person already exists in it
 * after acquiring the lock for that key.
 */	
@Component
public class TestHazelcast {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestHazelcast.class);
	
	@Autowired
	@Qualifier("hazelcastInstance")
	private HazelcastInstance hzInstance;
	
	public void addToHazelcastMap(Name name) {
		IMap<String, Name> hzMap = hzInstance.getMap("namesMap");
		ILock lock = hzInstance.getLock(name.getKey());
		lock.lock();
		if(!hzMap.containsKey(name.getKey())) {
			LOGGER.info("Adding name {} to hazelcast map", name);
			hzMap.put(name.getKey(), name);
		} else {
			LOGGER.info("Name {} already in map. Incrementing the age...", name);
			Name existingName = hzMap.get(name.getKey());
			existingName.setAge(existingName.getAge() + 1);
			hzMap.put(existingName.getKey(), existingName);
		}
		lock.unlock();
	}
	
	public void printHazelcastMap() {
		IMap<String, Name> hzMap = hzInstance.getMap("namesMap");
		for(Entry<String, Name> name : hzMap.entrySet()) {
			LOGGER.info("Key - {}, Name - {}", name.getKey(), name.getValue());
		}
	}
	
	public void assignDistributedTask(String key) throws InterruptedException, ExecutionException {
		IExecutorService exec = hzInstance.getExecutorService("testExecService");
		boolean result = exec.submitToKeyOwner(new DistributedTask().setKey(key), key).get();
		LOGGER.info("The result of DIstributed task was - ", result);
	}

}
