package com.test.kafka.producer;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.test.config.AppConfigVO;
import com.test.config.kafka.KafkaProperties;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SampleKafkaProducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(SampleKafkaProducer.class);
	
	@Autowired
	@Qualifier("kafkaProducer")
	private KafkaProducer<String, String> producer; 
	
	@Autowired
	private AppConfigVO appConfig;
	
	@Autowired
	private KafkaProperties kafkaProps;
	
	public void send(String key, String msg) {
		String topic = kafkaProps.getKafkaConfigVO(appConfig.getKafkaProducerConfig()).getTopic();
		LOGGER.info("Sending msg {} to topic {}", msg, topic);
		try {
			ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, msg);
			producer.send(record).get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("Exception occured: {}", e.getMessage());
		}
	}
	
	public void sendToSpecificPartition(String key, String msg, int partionNum) {
		String topic = kafkaProps.getKafkaConfigVO(appConfig.getKafkaProducerConfig()).getTopic();
		LOGGER.info("Sending msg {} to topic {}", msg, topic);
		try {
			ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, partionNum, key, msg);
			producer.send(record).get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("Exception occured: {}", e.getMessage());
		}
	}
	
	public void sendToAllPartitions(String key, String msg) {
		String topic = kafkaProps.getKafkaConfigVO(appConfig.getKafkaProducerConfig()).getTopic();
		LOGGER.info("Sending msg {} to topic {}", msg, topic);
		int numPartitions = producer.partitionsFor(topic).size();
		try {
			for(int i = 0; i< numPartitions; i++) {
				ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, i, key, msg);
				producer.send(record).get();
			}
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("Exception occured: {}", e.getMessage());
		}
	}
}
