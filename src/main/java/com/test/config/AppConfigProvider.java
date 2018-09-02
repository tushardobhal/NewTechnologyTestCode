package com.test.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.config.kafka.KafkaProperties;

@Configuration
public class AppConfigProvider {
	
	@Value("test.app.config.load")
	private String applicationConfig;
	
	@Autowired
	private AppConfigRepository repository;
	
	@Autowired
	private KafkaProperties kafkaProperties;
	
	@Bean("appConfig")
	public AppConfigVO appConfig() {
		return repository.findById(applicationConfig).isPresent() ? repository.findById(applicationConfig).get() : null;
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
	
	@Bean(name = "kafkaProducer")
	@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public KafkaProducer<String, String> kafkaProducer() {
		return new KafkaProducer<String, String>(kafkaProperties.getKafkaProperties(appConfig().getKafkaProducerConfig()));
	}
	
	@Bean(name = "kafkaConsumer")
	@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public KafkaConsumer<String, String> kafkaConsumer() {
		return new KafkaConsumer<String, String>(kafkaProperties.getKafkaProperties(appConfig().getKafkaProducerConfig()));
	}

}
