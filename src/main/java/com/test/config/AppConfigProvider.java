package com.test.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfigProvider {
	
	@Value("couchbase.cluster.uris")
	private List<String> baseURIs;
	
	@Value("couchbase.cluster.bucket.name")
	private String bucketName;
	
	@Bean(name = "couchbaseCluster", destroyMethod = "disconnect")
	public Cluster couchbaseCluster() {
		return CouchbaseCluster.create(baseURIs);
	}
	
	@Bean(name = "couchbaseBucket", destroyMethod = "close")
	public Bucket couchbaseBucket() {
		return couchbaseCluster().openBucket(bucketName);
	}
	
	@Bean(name = "asyncCouchbaseBucket", destroyMethod = "close")
	public AsyncBucket asyncCouchbaseBucket() {
		return couchbaseBucket().async();
	}
	
	@Bean(name = "jacksonMapper")
	public ObjectMapper jacksonMapper() {
		return new ObjectMapper();
	}

	public List<String> getBaseURIs() {
		return baseURIs;
	}

	public void setBaseURIs(List<String> baseURIs) {
		this.baseURIs = baseURIs;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
}
