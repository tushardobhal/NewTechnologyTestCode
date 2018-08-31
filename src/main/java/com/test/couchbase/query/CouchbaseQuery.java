package com.test.couchbase.query;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.config.AppConfigVO;
import com.test.dao.Name;

@Component
public class CouchbaseQuery {
	private static final Logger LOGGER = LoggerFactory.getLogger(CouchbaseQuery.class);

	@Autowired
	@Qualifier("couchbaseBucket")
	private Bucket bucket;
	
	@Autowired
	private AppConfigVO appConfig;
	
	@Autowired
	@Qualifier("jacksonMapper")
	private ObjectMapper mapper;
	
	public void query() {
		N1qlQueryResult result = bucket.query(
				N1qlQuery.simple("SELECT * FROM " + appConfig.getBucketName() + " WHERE age = '25'"), 20,
				TimeUnit.SECONDS);
		Iterator<N1qlQueryRow> itr = result.rows();
		LOGGER.info("{} results obtained", result.allRows().size());
		while(itr.hasNext()) {
			String val = itr.next().value().get(appConfig.getBucketName()).toString();
			try {
				Name name = mapper.readValue(val, Name.class);
				LOGGER.info("Object attained {}", name);
			} catch (IOException e) {
				LOGGER.error("IOException Occured - {}", e);
			}
		}
	}
}
