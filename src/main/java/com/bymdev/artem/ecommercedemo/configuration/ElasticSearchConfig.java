package com.bymdev.artem.ecommercedemo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .usingSsl("2bf56c6d71259cd2385bd36fe1593d5a5940e1c23b34639bef3c9b1492f630c6") //add the generated sha-256 fingerprint
                .withBasicAuth("elastic", "4SXmyR-w*+p=F6iHfJUY") //add your username and password
                .build();
    }
}
