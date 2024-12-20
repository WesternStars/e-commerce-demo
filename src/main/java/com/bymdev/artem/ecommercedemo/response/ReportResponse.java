package com.bymdev.artem.ecommercedemo.response;

import java.time.LocalDate;

public record ReportResponse(LocalDate date, Double sum) {
}
