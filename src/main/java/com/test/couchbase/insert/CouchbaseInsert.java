package com.test.couchbase.insert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

@Component
public class CouchbaseInsert {
	private static final Logger LOGGER = LoggerFactory.getLogger(CouchbaseInsert.class);
	
	@Autowired
	@Qualifier("couchbaseBucket")
	private Bucket bucket;
	
	public void insert() {
		try {
			long t1 = System.currentTimeMillis();
			JsonObject obj = JsonObject.empty();
			obj.put("firstName", "tushar");
			obj.put("lastName", "dobhal");
			obj.put("age",  23);
			JsonDocument doc = JsonDocument.create("name", obj);
			bucket.upsert(doc);
			LOGGER.info("Time Taken = {} ms", (System.currentTimeMillis() - t1));
		} catch (Exception e) {
			LOGGER.error("Error connecting to Couchbase: {}", e.getMessage());
		}
	}
}
