package com.test.config.kafka;

import com.couchbase.client.java.repository.annotation.Field;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StreamConfigVO {

	@Field
	private String bootstrapServers;
	@Field
	private String keySerde;
	@Field
	private String valueSerde;
	@Field
	private String applicationId;
	@Field
	private String inputTopic;
	@Field
	private String outputTopic;
	@Field
	private int numThreads;
	@Field
	private String type;
	
	@JsonProperty("bootstrap.servers")
	public String getBootstrapServers() {
		return bootstrapServers;
	}
	
	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}
	
	@JsonProperty("default.key.serde")
	public String getKeySerde() {
		return keySerde;
	}

	public void setKeySerde(String keySerde) {
		this.keySerde = keySerde;
	}

	@JsonProperty("default.value.serde")
	public String getValueSerde() {
		return valueSerde;
	}

	public void setValueSerde(String valueSerde) {
		this.valueSerde = valueSerde;
	}

	@JsonProperty("application.id")
	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getInputTopic() {
		return inputTopic;
	}

	public void setInputTopic(String inputTopic) {
		this.inputTopic = inputTopic;
	}

	public String getOutputTopic() {
		return outputTopic;
	}

	public void setOutputTopic(String outputTopic) {
		this.outputTopic = outputTopic;
	}

	@JsonProperty("num.stream.threads")
	public int getNumThreads() {
		return numThreads;
	}

	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

}
