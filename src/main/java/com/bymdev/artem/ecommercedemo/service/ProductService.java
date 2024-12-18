package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Product;
import com.bymdev.artem.ecommercedemo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProducts(int count, int page) {
        Iterable<Product> all = productRepository.findAll(PageRequest.of(page, count));
        List<Product> products = new ArrayList<>();
        all.forEach(products::add);
        return products;
    }

    public void createProduct(Product product) {
        productRepository.save(product);
    }

    public void updateProduct(Product product) {
        productRepository.save(product);
    }
}
