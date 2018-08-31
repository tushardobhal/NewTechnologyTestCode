package com.test.config;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Value("test.couchbase.config.load")
	private String couchbaseConfig;
	
	@Autowired
	private AppConfigRepository repository;
	
	@Bean("appConfig")
	public AppConfigVO appConfig() {
		return repository.findById(couchbaseConfig).isPresent() ? repository.findById(couchbaseConfig).get() : null;
	}
	
	@Bean(name = "couchbaseCluster", destroyMethod = "disconnect")
	public Cluster couchbaseCluster() {
		return CouchbaseCluster.create(appConfig().getBaseURIList());
	}
	
	@Bean(name = "couchbaseBucket", destroyMethod = "close")
	public Bucket couchbaseBucket() {
		return couchbaseCluster().openBucket(appConfig().getBucketName());
	}
	
	@Bean(name = "asyncCouchbaseBucket", destroyMethod = "close")
	public AsyncBucket asyncCouchbaseBucket() {
		return couchbaseBucket().async();
	}
	
	@Bean(name = "jacksonMapper")
	public ObjectMapper jacksonMapper() {
		return new ObjectMapper();
	}

}
