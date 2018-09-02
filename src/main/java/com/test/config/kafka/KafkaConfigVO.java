package com.test.config.kafka;

import com.couchbase.client.java.repository.annotation.Field;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KafkaConfigVO {

	@Field
	private String bootstrapServers;
	@Field
	private String zookeeperConnect;
	@Field
	private String keySerializer;
	@Field
	private String valueSerializer;
	@Field
	private String keyDeserializer;
	@Field
	private String valueDeserializer;
	@Field
	private String groupId;
	@Field
	private String topic;
	@Field
	private String type;
	
	@JsonProperty("bootstrap.servers")
	public String getBootstrapServers() {
		return bootstrapServers;
	}
	
	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}
	
	@JsonProperty("zookeeper.connect")
	public String getZookeeperConnect() {
		return zookeeperConnect;
	}
	
	public void setZookeeperConnect(String zookeeperConnect) {
		this.zookeeperConnect = zookeeperConnect;
	}
	
	@JsonProperty("key.serializer")
	public String getKeySerializer() {
		return keySerializer;
	}
	
	public void setKeySerializer(String keySerializer) {
		this.keySerializer = keySerializer;
	}
	
	@JsonProperty("value.serializer")
	public String getValueSerializer() {
		return valueSerializer;
	}
	
	public void setValueSerializer(String valueSerializer) {
		this.valueSerializer = valueSerializer;
	}
	
	@JsonProperty("key.deserializer")
	public String getKeyDeserializer() {
		return keyDeserializer;
	}

	public void setKeyDeserializer(String keyDeserializer) {
		this.keyDeserializer = keyDeserializer;
	}

	@JsonProperty("value.deserializer")
	public String getValueDeserializer() {
		return valueDeserializer;
	}

	public void setValueDeserializer(String valueDeserializer) {
		this.valueDeserializer = valueDeserializer;
	}

	@JsonProperty("group.id")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@JsonProperty("topic")
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

}
