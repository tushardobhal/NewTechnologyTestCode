package com.test.kafka.streams;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.dao.Name;

public class LowLevelStreamsProcessor extends AbstractProcessor<String, Object> {
	private static final Logger LOGGER = LoggerFactory.getLogger(LowLevelStreamsProcessor.class);

	private ProcessorContext context;
	private KeyValueStore<String, Integer> stateStore;
	
	 @Override
	  @SuppressWarnings("unchecked")
	  public void init(ProcessorContext context) {
	      this.context = context;
	      stateStore = (KeyValueStore<String, Integer>) context.getStateStore("sum-age");
	      
	      this.context.schedule(5000, PunctuationType.STREAM_TIME, (timestamp) -> {
	          KeyValueIterator<String, Integer> iter = this.stateStore.all();
	          while (iter.hasNext()) {
	              KeyValue<String, Integer> entry = iter.next();
	              LOGGER.info("Sending {} - {}", entry.key, entry.value);
	              context.forward(entry.key, entry.value.toString());
	          }
	          iter.close();
	          context.commit();
	      });
	  }
	 
	@Override
	public void process(String key, Object value) {
		Name name = (Name) value;
		Integer currAge = this.stateStore.get(name.getFirstName());
		if(currAge == null) {
			currAge = name.getAge();
		} else {
			currAge += name.getAge();
		}
		this.stateStore.put(name.getFirstName(), currAge);
		
		context.forward(name.getFirstName(), currAge.toString());
		context.commit();
	}
	
}
