package com.bymdev.artem.ecommercedemo.controller;

import com.bymdev.artem.ecommercedemo.entity.Category;
import com.bymdev.artem.ecommercedemo.request.CatalogRequest;
import com.bymdev.artem.ecommercedemo.service.CategoryService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    CategoryService categoryService;

    @GetMapping("/")
    public List<Category> getCategories(
            @Min(value = 1, message = "The count must be greater than 0") @RequestParam(value = "count", defaultValue = "100") int count,
            @Min (value = 0, message = "The page started from 0") @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return categoryService.getCategories(count, page);
    }

    @GetMapping("/{id}")
    public Category getCategory(@Positive(message = "The category id must be greater than 0") @PathVariable Integer id) {
        return categoryService.getCategory(id);
    }

    @PostMapping("/")
    @ResponseStatus(CREATED)
    public void createCategory(@Validated @RequestBody CatalogRequest request) {
        categoryService.createCategory(request);
    }

    @PutMapping("/{id}")
    public void updateCategory(@Positive(message = "The category id must be greater than 0") @PathVariable Integer id,
                               @Validated @RequestBody CatalogRequest request) {
        categoryService.updateCategory(id, request);
    }
}
