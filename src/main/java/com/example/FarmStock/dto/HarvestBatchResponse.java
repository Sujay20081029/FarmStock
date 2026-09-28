package com.example.FarmStock.dto;

import com.example.FarmStock.entity.HarvestBatch;
import java.time.LocalDate;

public record HarvestBatchResponse(Long id, Long cropId, String cropName, Integer quantity, LocalDate harvestDate) {

    public static HarvestBatchResponse from(HarvestBatch batch) {
        return new HarvestBatchResponse(
            batch.getId(),
            batch.getCrop().getId(),
            batch.getCrop().getName(),
            batch.getQuantity(),
            batch.getHarvestDate()
        );
    }
}
