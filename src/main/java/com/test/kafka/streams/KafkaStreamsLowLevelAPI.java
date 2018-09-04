package com.test.kafka.streams;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.test.config.AppConfigVO;
import com.test.config.kafka.KafkaProperties;
import com.test.config.kafka.StreamConfigVO;

/*
 * This sample program is to demonstrate Low Level Streams API
 * It gets Name object as value, sums the age by firstName
 * and finally sends the result as a timed window of 5 seconds
 */
public class KafkaStreamsLowLevelAPI {
private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamsLowLevelAPI.class);
	
	@Autowired
	private KafkaProperties streamProperties;
	
	@Autowired
	private AppConfigVO appConfig;
	
	public void startStream() {
		Topology builder = new Topology();
		StreamConfigVO streamConfigVO = streamProperties.getStreamConfigVO(appConfig.getStreamsConfig());
		Properties props = streamProperties.getStreamsProperties(appConfig.getStreamsConfig());
		
		StoreBuilder<KeyValueStore<String, Integer>> ageSumStore =
				    Stores.keyValueStoreBuilder(
				    Stores.persistentKeyValueStore("sum-age"),
				    Serdes.String(),
				    Serdes.Integer());
				
		builder.addSource("test-source", streamConfigVO.getInputTopic())
			   .addProcessor("sum-age-processor", LowLevelStreamsProcessor::new, "test-source")
			   .addStateStore(ageSumStore, "sum-age-processor")
			   .addSink("test-sink", streamConfigVO.getOutputTopic(), "sum-age-processor");
		
		KafkaStreams streams = new KafkaStreams(builder, props);
		streams.start();
		LOGGER.info("Low Level Streams started");
	}
}
