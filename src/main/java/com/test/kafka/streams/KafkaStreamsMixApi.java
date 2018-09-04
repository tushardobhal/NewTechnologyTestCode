package com.test.kafka.streams;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.test.config.AppConfigVO;
import com.test.config.kafka.KafkaProperties;
import com.test.config.kafka.StreamConfigVO;
import com.test.dao.Name;

/*
 * This sample program is to demonstrate Processor topology in DSL Streams API
 * It gets Name object as value, sums the age by firstName
 * and finally sends the result as a timed window of 5 seconds. 
 * Also, it uploads data to couchbase.
 */
public class KafkaStreamsMixApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamsMixApi.class);
	
	@Autowired
	private KafkaProperties streamProperties;
	
	@Autowired
	private AppConfigVO appConfig;
	
	@Autowired
	private MixLevelStreamsProcessor processor;
	
	public void startStream() {
		StreamsBuilder builder = new StreamsBuilder();
		StreamConfigVO streamConfigVO = streamProperties.getStreamConfigVO(appConfig.getStreamsConfig());
		Properties props = streamProperties.getStreamsProperties(appConfig.getStreamsConfig());
		
		KStream<String, Object> sampleStream = builder.stream(streamConfigVO.getInputTopic());
		
		StoreBuilder<KeyValueStore<String, Integer>> ageSumStore =
			    Stores.keyValueStoreBuilder(
			    Stores.persistentKeyValueStore("sum-age"),
			    Serdes.String(),
			    Serdes.Integer());
		builder.addStateStore(ageSumStore);
		
		sampleStream
				.filter((k, v) -> v != null)
				.mapValues(v -> (Name) v)
				.process(() -> processor, "sum-age");
		
		KafkaStreams streams = new KafkaStreams(builder.build(), props);
		streams.start();
		LOGGER.info("High Level DSL Streams started");
	}
}
