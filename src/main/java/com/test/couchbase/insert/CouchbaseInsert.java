package com.test.couchbase.insert;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

public class CouchbaseInsert {
	private static final Logger LOGGER = LoggerFactory.getLogger(CouchbaseInsert.class);
	
	public static void main(String[] args) {
		try {
			long t1 = System.currentTimeMillis();
			/*URI local = new URI("http://localhost:8091/pools");
		      List<URI> baseURIs = new ArrayList<URI>();
		      baseURIs.add(local);*/

			List<String> baseURIs = new ArrayList<String>();
			baseURIs.add("http://localhost:8091");

			Cluster cluster = CouchbaseCluster.create(baseURIs);
			Bucket bucket = cluster.openBucket("beer-sample");
			JsonObject obj = JsonObject.empty();
			obj.put("firstName", "tushar");
			obj.put("lastName", "dobhal");
			obj.put("age",  23);
			JsonDocument doc =JsonDocument.create("name", obj);
			bucket.upsert(doc);
			LOGGER.info("Time Taken = {} ms", (System.currentTimeMillis() - t1));
		} catch (Exception e) {
			LOGGER.error("Error connecting to Couchbase: {}", e.getMessage());
		}
	}
}
