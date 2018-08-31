package com.test.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

@ViewIndexed(designDoc = "appConfigVo")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public interface AppConfigRepository extends CouchbaseRepository<AppConfigVO, String> {
 
}
