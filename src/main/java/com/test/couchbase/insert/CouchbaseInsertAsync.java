package com.test.couchbase.insert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.couchbase.client.core.time.Delay;
import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.util.retry.RetryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.dao.Name;

import rx.Observable;

public class CouchbaseInsertAsync {
private static final Logger LOGGER = LoggerFactory.getLogger(CouchbaseInsert.class);
	
	@Autowired
	@Qualifier("asyncCouchbaseBucket")
	private AsyncBucket asyncbucket;
	
	@Autowired
	@Qualifier("jacksonMapper")
	private ObjectMapper mapper;
	
	@SuppressWarnings("unchecked")
	public void insertAsync() {
		Name name = new Name();
		name.setAge(25);
		name.setFirstName("Tushar");
		name.setLastName("Dobhal");
		List<Name> names = new ArrayList<>(Arrays.asList(name));
		
		Observable.from(names).flatMap(record -> {
			onEachRecordAction(record);
			return asyncbucket.upsert(convert(record));
		}, error -> {
			onErrorAction(error);
			return null;
		}, () -> {
			onBulkCompletedAction(names.size());
			return null;
		}).retryWhen(RetryBuilder.allBut(DocumentAlreadyExistsException.class)
				.delay(Delay.exponential(TimeUnit.SECONDS, 10)).max(5).build()).toBlocking().last();
	}
	
	private JsonDocument convert(Name name) {
		try {
			return JsonDocument.create(Integer.toString(name.hashCode()), JsonObject.fromJson(mapper.writeValueAsString(name)));
		} catch (JsonProcessingException e) {
			LOGGER.error("JsonProcessingException occured while converting to JsonDocument - {}", e.getMessage());
		}
		return null;
	}
	
	private void onEachRecordAction(Name name) {
		LOGGER.info("Upserting {}", name);
	}
	
	private void onErrorAction(Throwable error) {
		LOGGER.error("Exception occured while inserting - {}", error.getMessage());
	}
	
	private void onBulkCompletedAction(int size) {
		LOGGER.info("Completed bulk inserts for {} names", size);
	}
	
}
