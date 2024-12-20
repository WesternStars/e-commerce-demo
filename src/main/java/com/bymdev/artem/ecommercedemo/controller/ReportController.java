package com.bymdev.artem.ecommercedemo.controller;

import com.bymdev.artem.ecommercedemo.response.ReportResponse;
import com.bymdev.artem.ecommercedemo.service.ReportService;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/in_come_by_a_day")
    public List<ReportResponse> getCategories(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @Min(value = 1, message = "The count must be greater than 0") @RequestParam(value = "count", defaultValue = "100") int count,
            @Min(value = 0, message = "The page started from 0") @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return reportService.getInComeByADayReport(from, to, count, page);
    }
}
