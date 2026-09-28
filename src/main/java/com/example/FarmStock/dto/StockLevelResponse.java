package com.example.FarmStock.dto;

public record StockLevelResponse(Long cropId, String cropName, Integer currentStock) {
}
