package com.test.couchbase.query;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.couchbase.client.core.CouchbaseException;
import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryRow;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.SimpleN1qlQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.config.AppConfigProvider;
import com.test.dao.Name;

import rx.Observable;

@Component
public class CouchbaseQueryAsync {
private static final Logger LOGGER = LoggerFactory.getLogger(CouchbaseQueryAsync.class);
	
	@Autowired
	private AppConfigProvider appConfig;

	@Autowired
	@Qualifier("asyncCouchbaseBucket")
	private AsyncBucket asyncBucket;
	
	@Autowired
	@Qualifier("jacksonMapper")
	private ObjectMapper mapper;
	
	public void queryAsync() {
		SimpleN1qlQuery n1qlQuery = N1qlQuery.simple("SELECT * FROM " + appConfig.getBucketName() + " WHERE age = '25'");
		asyncBucket.query(n1qlQuery)
				.flatMap(result -> result.errors()
						.flatMap(e -> Observable.<AsyncN1qlQueryRow>error(new CouchbaseException("N1QL Error" + e)))
						.switchIfEmpty(result.rows()))
				.map(AsyncN1qlQueryRow::value)
				.subscribe(this::subscribeAction, this::errorAction);
	}
	
	private void subscribeAction(JsonObject value) {
		String res = value.get(appConfig.getBucketName()).toString();
		try {
			Name name = mapper.readValue(res, Name.class);
			LOGGER.info("Object attained {}", name);
		} catch (IOException e) {
			LOGGER.error("IOException Occured - {}", e);
		}
	}
	
	private void errorAction(Throwable error) {
		LOGGER.error("Exception occured while inserting - {}", error.getMessage());
	}
}
