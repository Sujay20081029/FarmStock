package com.example.FarmStock.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SaleRequest(
    @NotNull(message = "Crop id is required") Long cropId,
    @NotNull(message = "Quantity is required") @Positive(message = "Quantity must be positive") Integer quantity,
    @NotNull(message = "Sale date is required") LocalDate saleDate,
    @NotNull(message = "Total price is required") @Positive(message = "Total price must be positive") BigDecimal totalPrice
) {
}
