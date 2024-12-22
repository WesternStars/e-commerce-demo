package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Category;
import com.bymdev.artem.ecommercedemo.entity.Product;
import com.bymdev.artem.ecommercedemo.repository.CategoryRepository;
import com.bymdev.artem.ecommercedemo.repository.OrderItemRepository;
import com.bymdev.artem.ecommercedemo.repository.ProductRepository;
import com.bymdev.artem.ecommercedemo.repository.SearchRepository;
import com.bymdev.artem.ecommercedemo.request.ProductRequest;
import com.bymdev.artem.ecommercedemo.response.ProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final SearchService searchService;
    private final SearchRepository searchRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderItemRepository orderItemRepository;

    public static ProductResponse getProductResponse(Product product) {
        return new ProductResponse(
                product.getSku(),
                product.getName(),
                product.getPrice(),
                CategoryService.getCategoryResponse(product.getCategory())
        );
    }

    public ProductResponse getProduct(String sku) {
        return getProductResponse(productRepository.findById(sku).orElseThrow());
    }

    public List<ProductResponse> getProducts(int count, int page) {
        return productRepository.findAll(PageRequest.of(page, count))
                .stream()
                .map(ProductService::getProductResponse)
                .toList();
    }

    public ProductResponse createProduct(ProductRequest request) {
        return getProductResponse(productRepository.save(getProduct(request)));
    }

    public ProductResponse updateProduct(ProductRequest request) {
        productRepository.findById(request.sku()).orElseThrow();
        Product product = getProduct(request);
        Product saved = productRepository.save(product);
        searchRepository.save(searchService.getOrderDoc(saved));
        return getProductResponse(saved);
    }

    public void deleteProduct(String sku) {
        if (!orderItemRepository.findAllByProduct_Sku(sku).isEmpty()) {
            throw new RuntimeException("You should delete all dependencies of this product.");
        }
        productRepository.deleteById(sku);
    }

    private Product getProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId()).orElseThrow();
        return new Product(request.sku(), request.name(), request.price(), category);
    }
}
