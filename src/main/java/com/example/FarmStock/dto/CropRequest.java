package com.example.FarmStock.dto;

import jakarta.validation.constraints.NotBlank;

public record CropRequest(
    @NotBlank(message = "Crop name is required") String name
) {
}
