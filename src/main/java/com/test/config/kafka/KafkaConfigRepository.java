package com.test.config.kafka;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

@ViewIndexed(designDoc = "kafkaConfigVo")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public interface KafkaConfigRepository extends CouchbaseRepository<KafkaConfigVO, String> {
 
}
