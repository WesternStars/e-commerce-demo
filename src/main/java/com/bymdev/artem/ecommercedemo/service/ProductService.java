package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Category;
import com.bymdev.artem.ecommercedemo.entity.Product;
import com.bymdev.artem.ecommercedemo.repository.CategoryRepository;
import com.bymdev.artem.ecommercedemo.repository.ProductRepository;
import com.bymdev.artem.ecommercedemo.request.ProductAddOrUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Product getProduct(String sku) {
        return productRepository.findById(sku).orElseThrow();
    }

    public List<Product> getProducts(int count, int page) {
        Iterable<Product> all = productRepository.findAll(PageRequest.of(page, count));
        List<Product> products = new ArrayList<>();
        all.forEach(products::add);
        return products;
    }

    public void createOrUpdateProduct(ProductAddOrUpdateRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = new Product(request.sku(), request.name(), request.price(), category);
        productRepository.save(product);
    }
}
