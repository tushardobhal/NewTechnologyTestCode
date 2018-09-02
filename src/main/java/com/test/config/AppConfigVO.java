package com.test.config;

import java.util.Arrays;
import java.util.List;

import com.couchbase.client.java.repository.annotation.Field;

public class AppConfigVO {

	@Field
	private String baseURIs;
	@Field
	private String bucketName;
	@Field
	private String kafkaProducerConfig;
	@Field 
	private String kafkaConsumerConfig;
	@Field
	private String type;
	
	public String getBaseURIs() {
		return baseURIs;
	}
	
	public void setBaseURIs(String baseURIs) {
		this.baseURIs = baseURIs;
	}
	
	public List<String> getBaseURIList() {
		return Arrays.asList(baseURIs.split(","));
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	public String getKafkaProducerConfig() {
		return kafkaProducerConfig;
	}

	public void setKafkaProducerConfig(String kafkaProducerConfig) {
		this.kafkaProducerConfig = kafkaProducerConfig;
	}

	public String getKafkaConsumerConfig() {
		return kafkaConsumerConfig;
	}

	public void setKafkaConsumerConfig(String kafkaConsumerConfig) {
		this.kafkaConsumerConfig = kafkaConsumerConfig;
	}
	
	public String getType() {
		return type;
	}

	public void seType(String type) {
		this.type = type;
	}

}
