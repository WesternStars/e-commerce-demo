package com.bymdev.artem.ecommercedemo.repository;

import com.bymdev.artem.ecommercedemo.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, String>, PagingAndSortingRepository<Product, String> {
}
