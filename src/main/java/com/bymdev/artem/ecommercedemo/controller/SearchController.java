package com.bymdev.artem.ecommercedemo.controller;

import com.bymdev.artem.ecommercedemo.response.OrderResponse;
import com.bymdev.artem.ecommercedemo.service.SearchService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/ordersByProductName")
    public List<OrderResponse> searchOrdersByProductName(
            @Size(min = 3, message = "The namePart must be greater than 2") @RequestParam(name = "name_part") String namePart,
            @Min(value = 1, message = "The count must be greater than 0") @RequestParam(value = "count", defaultValue = "100") int count,
            @Min(value = 0, message = "The page started from 0") @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return searchService.search(namePart, page, count);
    }

    @GetMapping("/ordersByProductName/reindex")
    public String reindex() {
        searchService.reindex();
        return "order_index was updated.";
    }
}
