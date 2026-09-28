package com.example.FarmStock.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record HarvestBatchRequest(
    @NotNull(message = "Crop id is required") Long cropId,
    @NotNull(message = "Quantity is required") @Positive(message = "Quantity must be positive") Integer quantity,
    @NotNull(message = "Harvest date is required") LocalDate harvestDate
) {
}
