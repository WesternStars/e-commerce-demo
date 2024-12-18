package com.bymdev.artem.ecommercedemo.controller;

import com.bymdev.artem.ecommercedemo.entity.Category;
import com.bymdev.artem.ecommercedemo.request.CreateCatalogRequest;
import com.bymdev.artem.ecommercedemo.request.UpdateCatalogRequest;
import com.bymdev.artem.ecommercedemo.service.CategoryService;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    CategoryService categoryService;

    @GetMapping("/")
    public List<Category> getCategory(
            @Min(value = 1, message = "The count must be greater than 0") @RequestParam(value = "count", defaultValue = "100") int count,
            @Min (value = 0, message = "The page started from 0") @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return categoryService.getCategory(count, page);
    }

    @PostMapping("/")
    @ResponseStatus(CREATED)
    public void createCategory(@Validated @RequestBody CreateCatalogRequest request) {
        categoryService.createCategory(request);
    }

    @PutMapping("/")
    public void updateCategory(@Validated @RequestBody UpdateCatalogRequest request) {
        categoryService.updateCategory(request);
    }
}
