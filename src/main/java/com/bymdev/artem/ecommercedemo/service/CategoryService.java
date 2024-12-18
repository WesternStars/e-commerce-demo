package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Category;
import com.bymdev.artem.ecommercedemo.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategory(int count, int page) {
        Iterable<Category> all = categoryRepository.findAll(PageRequest.of(page, count));
        List<Category> categories = new ArrayList<>();
        all.forEach(categories::add);
        return categories;
    }

    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }
}
