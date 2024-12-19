package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Category;
import com.bymdev.artem.ecommercedemo.repository.CategoryRepository;
import com.bymdev.artem.ecommercedemo.repository.ProductRepository;
import com.bymdev.artem.ecommercedemo.request.CatalogRequest;
import com.bymdev.artem.ecommercedemo.response.CategoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryResponse getCategory(int id) {
        Category category = categoryRepository.findById(id).orElseThrow();
        return new CategoryResponse(category.getId(), category.getName());
    }

    public List<CategoryResponse> getCategories(int count, int page) {
        return categoryRepository.findAll(PageRequest.of(page, count))
                .stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .toList();
    }

    public CategoryResponse createCategory(CatalogRequest request) {
        Category category = categoryRepository.save(new Category(null, request.name()));
        return new CategoryResponse(category.getId(), category.getName());
    }

    public CategoryResponse updateCategory(int id, CatalogRequest request) {
        categoryRepository.findById(id).orElseThrow();
        Category category = categoryRepository.save(new Category(id, request.name()));
        return new CategoryResponse(category.getId(), category.getName());
    }

    public void deleteCategory(int id) {
        if (!productRepository.findAllByCategory_Id(id).isEmpty()) {
            throw new RuntimeException("You should delete all dependencies of this category.");
        }
        categoryRepository.deleteById(id);
    }
}
