package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Category;
import com.bymdev.artem.ecommercedemo.repository.CategoryRepository;
import com.bymdev.artem.ecommercedemo.request.CatalogRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category getCategory(int id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    public List<Category> getCategories(int count, int page) {
        Iterable<Category> all = categoryRepository.findAll(PageRequest.of(page, count));
        List<Category> categories = new ArrayList<>();
        all.forEach(categories::add);
        return categories;
    }

    public void createCategory(CatalogRequest request) {
        categoryRepository.save(new Category(null, request.name()));
    }

    public void updateCategory(int id, CatalogRequest request) {
        categoryRepository.save(new Category(id, request.name()));
    }
}
