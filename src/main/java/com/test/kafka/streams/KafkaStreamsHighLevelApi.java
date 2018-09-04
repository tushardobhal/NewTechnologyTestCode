package com.test.kafka.streams;

import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.test.config.AppConfigVO;
import com.test.config.kafka.KafkaProperties;
import com.test.config.kafka.StreamConfigVO;
import com.test.dao.Name;

/*
 * This sample program is to demonstrate DSL Streams API
 * It gets Name object as value, sums the age by firstName
 * and finally sends the result as a timed window of 5 seconds
 */
public class KafkaStreamsHighLevelApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamsHighLevelApi.class);
	
	@Autowired
	private KafkaProperties streamProperties;
	
	@Autowired
	private AppConfigVO appConfig;
	
	public void startStream() {
		StreamsBuilder builder = new StreamsBuilder();
		StreamConfigVO streamConfigVO = streamProperties.getStreamConfigVO(appConfig.getStreamsConfig());
		Properties props = streamProperties.getStreamsProperties(appConfig.getStreamsConfig());
		
		KStream<String, Object> sampleTable = builder.stream(streamConfigVO.getInputTopic());
		
		sampleTable
				.filter((k, v) -> v != null)
				.mapValues(v -> (Name) v)
				.groupBy((k, v) -> KeyValue.pair(v.getFirstName(), v))
				.windowedBy(TimeWindows.of(5000))
				.aggregate(Name::new, (k, v, sum) -> {
						Name sumName = (Name) sum;
						sumName.setAge(sumName.getAge() + v.getAge());
						return sumName;
				})
				.toStream().map((k, v) -> new KeyValue<>(k, v.getAge()))
				.to(streamConfigVO.getOutputTopic());
		
		KafkaStreams streams = new KafkaStreams(builder.build(), props);
		streams.start();
		LOGGER.info("High Level DSL Streams started");
	}
}
