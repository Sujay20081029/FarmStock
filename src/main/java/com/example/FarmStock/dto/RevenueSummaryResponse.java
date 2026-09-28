package com.example.FarmStock.dto;

import java.math.BigDecimal;

public record RevenueSummaryResponse(Long cropId, String cropName, BigDecimal totalRevenue) {

    public static RevenueSummaryResponse fromRow(Object[] row) {
        return new RevenueSummaryResponse(
            ((Number) row[0]).longValue(),
            (String) row[1],
            row[2] == null ? BigDecimal.ZERO : new BigDecimal(row[2].toString())
        );
    }
}
