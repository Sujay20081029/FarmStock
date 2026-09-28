package com.example.FarmStock.dto;

import com.example.FarmStock.entity.Sale;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SaleResponse(Long id, Long cropId, String cropName, Integer quantity, LocalDate saleDate, BigDecimal totalPrice) {

    public static SaleResponse from(Sale sale) {
        return new SaleResponse(
            sale.getId(),
            sale.getCrop().getId(),
            sale.getCrop().getName(),
            sale.getQuantity(),
            sale.getSaleDate(),
            sale.getTotalPrice()
        );
    }
}
