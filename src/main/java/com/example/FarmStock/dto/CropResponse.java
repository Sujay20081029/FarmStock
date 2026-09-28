package com.example.FarmStock.dto;

import com.example.FarmStock.entity.Crop;

public record CropResponse(Long id, String name) {

    public static CropResponse from(Crop crop) {
        return new CropResponse(crop.getId(), crop.getName());
    }
}
