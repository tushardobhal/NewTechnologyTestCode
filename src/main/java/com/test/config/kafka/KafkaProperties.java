package com.test.config.kafka;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KafkaProperties {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProperties.class);
	
	@Autowired
	private KafkaConfigRepository kafkaRepository;
	
	@Autowired
	private StreamsConfigRepository streamsRepository;
	
	private Map<String, KafkaConfigVO> kafkaConfigs = new HashMap<>();
	private Map<String, StreamConfigVO> streamsConfigs = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getKafkaProperties(String id) {
		ObjectMapper mapper = new ObjectMapper();
		KafkaConfigVO kafkaConfigVO = getKafkaConfigVO(id);
		Map<String, Object> config = new HashMap<>();
		String json;
		try {
			json = mapper.writeValueAsString(kafkaConfigVO);
			config = mapper.readValue(json, config.getClass());
		} catch (JsonProcessingException e) {
			LOGGER.error("JsonProcessingException occured: {}", e.getMessage());
		} catch (IOException e) {
			LOGGER.error("IOException occured: {}", e.getMessage());
		} catch(Exception e) {
			LOGGER.error("Exception occured: {}", e.getMessage());
		}
		return config;
	}
	
	public KafkaConfigVO getKafkaConfigVO(String id) {
		LOGGER.info("Getting KAfka Config for {}", id);
		if(!kafkaConfigs.containsKey(id)) {
			KafkaConfigVO kafkaConfigVO = kafkaRepository.findById(id).isPresent() ? kafkaRepository.findById(id).get() : null;
			kafkaConfigs.put(id, kafkaConfigVO);
		}
		return kafkaConfigs.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public Properties getStreamsProperties(String id) {
		ObjectMapper mapper = new ObjectMapper();
		StreamConfigVO kafkaConfigVO = getStreamConfigVO(id);
		Map<String, Object> config = new HashMap<>();
		String json;
		try {
			json = mapper.writeValueAsString(kafkaConfigVO);
			config = mapper.readValue(json, config.getClass());
		} catch (JsonProcessingException e) {
			LOGGER.error("JsonProcessingException occured: {}", e.getMessage());
		} catch (IOException e) {
			LOGGER.error("IOException occured: {}", e.getMessage());
		} catch(Exception e) {
			LOGGER.error("Exception occured: {}", e.getMessage());
		}
		Properties props = new Properties();
		props.putAll(config);
		return props;
	}
	
	public StreamConfigVO getStreamConfigVO(String id) {
		LOGGER.info("Getting Streams Config for {}", id);
		if(!streamsConfigs.containsKey(id)) {
			StreamConfigVO kafkaConfigVO = streamsRepository.findById(id).isPresent() ? streamsRepository.findById(id).get() : null;
			streamsConfigs.put(id, kafkaConfigVO);
		}
		return streamsConfigs.get(id);
	}
}
