package com.bymdev.artem.ecommercedemo.controller;

import com.bymdev.artem.ecommercedemo.request.ProductRequest;
import com.bymdev.artem.ecommercedemo.response.ProductResponse;
import com.bymdev.artem.ecommercedemo.service.ProductService;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public List<ProductResponse> getProducts(
            @Min (value = 1, message = "The count must be greater than 0") @RequestParam(value = "count", defaultValue = "20") int count,
            @Min (value = 0, message = "The page started from 0") @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return productService.getProducts(count, page);
    }

    @GetMapping("/{sku}")
    public ProductResponse getProduct(@PathVariable String sku) {
        return productService.getProduct(sku);
    }

    @PostMapping("/")
    @ResponseStatus(CREATED)
    public ProductResponse createProduct(@Validated @RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    @PutMapping("/")
    public ProductResponse updateProduct(@Validated @RequestBody ProductRequest request) {
        return productService.updateProduct(request);
    }

    @DeleteMapping("/{sku}")
    public void deleteProduct(@PathVariable String sku) {
        productService.deleteProduct(sku);
    }

}
