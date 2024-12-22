package com.bymdev.artem.ecommercedemo.repository;

import com.bymdev.artem.ecommercedemo.entity.OrderDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchRepository extends ElasticsearchRepository<OrderDoc, Long> {

    @Query("""
            {
                "match": {
                    "productName": "?0"
                }
            }
            """)
    Page<OrderDoc> findAllByPartProductName(String productName, Pageable pageable);
}
