package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Category;
import com.bymdev.artem.ecommercedemo.entity.Product;
import com.bymdev.artem.ecommercedemo.repository.CategoryRepository;
import com.bymdev.artem.ecommercedemo.repository.OrderItemRepository;
import com.bymdev.artem.ecommercedemo.repository.ProductRepository;
import com.bymdev.artem.ecommercedemo.request.ProductRequest;
import com.bymdev.artem.ecommercedemo.response.CategoryResponse;
import com.bymdev.artem.ecommercedemo.response.ProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderItemRepository orderItemRepository;

    public ProductResponse getProduct(String sku) {
        Product product = productRepository.findById(sku).orElseThrow();
        return new ProductResponse(
                product.getSku(),
                product.getName(),
                product.getPrice(),
                new CategoryResponse(product.getCategory().getId(), product.getCategory().getName())
        );
    }

    public List<ProductResponse> getProducts(int count, int page) {
        return productRepository.findAll(PageRequest.of(page, count))
                .stream()
                .map(product -> new ProductResponse(
                        product.getSku(),
                        product.getName(),
                        product.getPrice(),
                        new CategoryResponse(product.getCategory().getId(), product.getCategory().getName())
                ))
                .toList();
    }

    public ProductResponse createProduct(ProductRequest request) {
        return saveProduct(request);
    }

    public ProductResponse updateProduct(ProductRequest request) {
        productRepository.findById(request.sku()).orElseThrow();
        return saveProduct(request);
    }

    public void deleteProduct(String sku) {
        if (!orderItemRepository.findAllByProduct_Sku(sku).isEmpty()) {
            throw new RuntimeException("You should delete all dependencies of this product.");
        }
        productRepository.deleteById(sku);
    }

    private ProductResponse saveProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId()).orElseThrow();
        Product product = new Product(request.sku(), request.name(), request.price(), category);
        product = productRepository.save(product);
        return new ProductResponse(
                product.getSku(),
                product.getName(),
                product.getPrice(),
                new CategoryResponse(product.getCategory().getId(), product.getCategory().getName())
        );
    }
}
