package com.test.kafka.consumer;

import java.util.Arrays;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
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
public class SampleKafkaConsumer implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(SampleKafkaConsumer.class);
	
	@Autowired
	@Qualifier("kafkaConsumer")
	private KafkaConsumer<String, String> consumer; 
	
	@Autowired
	private AppConfigVO appConfig;
	
	@Autowired
	private KafkaProperties kafkaProps;
	
	public void run() {
		String topic = kafkaProps.getKafkaConfigVO(appConfig.getKafkaProducerConfig()).getTopic();
		LOGGER.info("Listening to topic {}", topic);
		consumer.subscribe(Arrays.asList(topic));
		try {
			while(true) {
				ConsumerRecords<String, String> records = consumer.poll(0);
				for(ConsumerRecord<String, String> record : records) {
					consumer.commitAsync();
					LOGGER.info("Message Received - Key: {}, Value: {}", record.key(), record.value());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured: {}", e.getMessage());
		}
	}
	
}
