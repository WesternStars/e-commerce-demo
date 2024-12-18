package com.bymdev.artem.ecommercedemo.controller;

import com.bymdev.artem.ecommercedemo.entity.Category;
import com.bymdev.artem.ecommercedemo.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public List<Category> getCategory(
            @RequestParam(value = "count", defaultValue = "100") int count,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return categoryService.getCategory(count, page);
    }

    @PostMapping("/")
    @ResponseStatus(CREATED)
    public void createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
    }

    @PutMapping("/")
    public void updateCategory(@RequestBody Category category) {
        categoryService.updateCategory(category);
    }
}
