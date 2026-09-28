package com.example.FarmStock.controller;

import com.example.FarmStock.dto.RevenueSummaryResponse;
import com.example.FarmStock.dto.SaleRequest;
import com.example.FarmStock.dto.SaleResponse;
import com.example.FarmStock.service.CropService;
import com.example.FarmStock.service.SaleService;
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
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;
    private final CropService cropService;

    public SaleController(SaleService saleService, CropService cropService) {
        this.saleService = saleService;
        this.cropService = cropService;
    }

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody SaleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.createSale(request));
    }

    @GetMapping
    public List<SaleResponse> getAllSales() {
        return saleService.getAllSales();
    }

    @PutMapping("/{id}")
    public SaleResponse updateSale(@PathVariable Long id, @Valid @RequestBody SaleRequest request) {
        return saleService.updateSale(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/revenue")
    public List<RevenueSummaryResponse> getRevenueByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return cropService.getRevenueByCropRange(startDate, endDate);
    }
}
