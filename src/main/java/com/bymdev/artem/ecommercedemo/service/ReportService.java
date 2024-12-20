package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.repository.OrderRepository;
import com.bymdev.artem.ecommercedemo.response.ReportResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReportService {

    public static final String DATA_COLUMN_NAME = "data";
    public static final String SUM_COLUMN_NAME = "sum";
    private final OrderRepository orderRepository;

    public List<ReportResponse> getInComeByADayReport(LocalDate from, LocalDate to, int count, int page) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("from cannot be greater than to");
        }
        return orderRepository.findNSumReportsByDayWithPagination(from, PageRequest.of(page, count))
                .stream()
                .map(this::matToResponse)
                .toList();
    }

    private ReportResponse matToResponse(Map<String, Object> row) {
        return new ReportResponse(
                LocalDate.parse(row.get(DATA_COLUMN_NAME).toString()),
                Double.valueOf(row.get(SUM_COLUMN_NAME).toString())
        );
    }
}
