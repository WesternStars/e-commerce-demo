package com.bymdev.artem.ecommercedemo.controller;

import com.bymdev.artem.ecommercedemo.entity.Product;
import com.bymdev.artem.ecommercedemo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public List<Product> getProducts(
            @RequestParam(value = "count", defaultValue = "20") int count,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return productService.getProducts(count, page);
    }

    @PostMapping("/")
    @ResponseStatus(CREATED)
    public void creatProduct(@RequestBody Product product) {
        productService.creatProduct(product);
    }

    @PutMapping("/")
    public void updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
    }

}
