package com.bymdev.artem.ecommercedemo.controller;

import com.bymdev.artem.ecommercedemo.entity.Product;
import com.bymdev.artem.ecommercedemo.request.ProductAddOrUpdateRequest;
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
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public List<Product> getProducts(
            @Min (value = 1, message = "The count must be greater than 0") @RequestParam(value = "count", defaultValue = "20") int count,
            @Min (value = 0, message = "The page started from 0") @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return productService.getProducts(count, page);
    }

    @PostMapping("/")
    @ResponseStatus(CREATED)
    public void createProduct(@Validated @RequestBody ProductAddOrUpdateRequest request) {
        productService.createOrUpdateProduct(request);
    }

    @PutMapping("/")
    public void updateProduct(@Validated @RequestBody ProductAddOrUpdateRequest request) {
        productService.createOrUpdateProduct(request);
    }

}
