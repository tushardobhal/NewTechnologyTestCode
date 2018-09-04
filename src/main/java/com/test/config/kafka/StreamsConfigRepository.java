package com.test.config.kafka;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

@ViewIndexed(designDoc = "streamConfigVo")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public interface StreamsConfigRepository extends CouchbaseRepository<StreamConfigVO, String> {
 
}
