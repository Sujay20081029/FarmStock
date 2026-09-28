package com.example.FarmStock.controller;

import com.example.FarmStock.dto.CropRequest;
import com.example.FarmStock.dto.CropResponse;
import com.example.FarmStock.dto.RevenueSummaryResponse;
import com.example.FarmStock.dto.StockLevelResponse;
import com.example.FarmStock.service.CropService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crops")
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @PostMapping
    public ResponseEntity<CropResponse> createCrop(@Valid @RequestBody CropRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cropService.createCrop(request));
    }

    @GetMapping
    public List<CropResponse> getAllCrops() {
        return cropService.getAllCrops();
    }

    @GetMapping("/{id}")
    public CropResponse getCropById(@PathVariable Long id) {
        return cropService.getCropById(id);
    }

    @PutMapping("/{id}")
    public CropResponse updateCrop(@PathVariable Long id, @Valid @RequestBody CropRequest request) {
        return cropService.updateCrop(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCrop(@PathVariable Long id) {
        cropService.deleteCrop(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stock")
    public List<StockLevelResponse> getAllStockLevels() {
        return cropService.getAllStockLevels();
    }

    @GetMapping("/{id}/stock")
    public StockLevelResponse getStockLevelByCropId(@PathVariable Long id) {
        return cropService.getStockLevelByCropId(id);
    }

    @GetMapping("/{id}/revenue")
    public RevenueSummaryResponse getRevenueByCrop(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return cropService.getRevenueByCrop(id, startDate, endDate);
    }
}
