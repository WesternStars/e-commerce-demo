package com.bymdev.artem.ecommercedemo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(System.getProperty("elasticsearch.host"))
                .usingSsl(System.getProperty("elasticsearch.caFingerprint")) //add the generated sha-256 fingerprint
                .withBasicAuth(System.getProperty("elasticsearch.user"), System.getProperty("elasticsearch.password")) //add your username and password
                .build();
    }
}
