package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Category;
import com.bymdev.artem.ecommercedemo.entity.Product;
import com.bymdev.artem.ecommercedemo.repository.CategoryRepository;
import com.bymdev.artem.ecommercedemo.repository.OrderItemRepository;
import com.bymdev.artem.ecommercedemo.repository.ProductRepository;
import com.bymdev.artem.ecommercedemo.request.ProductRequest;
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
        return mapToResponse(productRepository.findById(sku).orElseThrow());
    }

    public List<ProductResponse> getProducts(int count, int page) {
        return productRepository.findAll(PageRequest.of(page, count))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse createProduct(ProductRequest request) {
        return saveProduct(mapToProduct(request));
    }

    public ProductResponse updateProduct(ProductRequest request) {
        productRepository.findById(request.sku()).orElseThrow();
        return saveProduct(mapToProduct(request));
    }

    public void deleteProduct(String sku) {
        if (!orderItemRepository.findAllByProduct_Sku(sku).isEmpty()) {
            throw new RuntimeException("You should delete all dependencies of this product.");
        }
        productRepository.deleteById(sku);
    }

    private Product mapToProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId()).orElseThrow();
        return new Product(request.sku(), request.name(), request.price(), category);
    }

    private ProductResponse saveProduct(Product product) {
        return mapToResponse(productRepository.save(product));
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getSku(),
                product.getName(),
                product.getPrice(),
                CategoryService.mapToResponse(product.getCategory())
        );
    }
}
