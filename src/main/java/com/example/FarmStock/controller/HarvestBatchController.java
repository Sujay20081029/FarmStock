package com.example.FarmStock.controller;

import com.example.FarmStock.dto.HarvestBatchRequest;
import com.example.FarmStock.dto.HarvestBatchResponse;
import com.example.FarmStock.service.HarvestBatchService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/harvest-batches")
public class HarvestBatchController {

    private final HarvestBatchService harvestBatchService;

    public HarvestBatchController(HarvestBatchService harvestBatchService) {
        this.harvestBatchService = harvestBatchService;
    }

    @PostMapping
    public ResponseEntity<HarvestBatchResponse> createHarvestBatch(@Valid @RequestBody HarvestBatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(harvestBatchService.createHarvestBatch(request));
    }

    @GetMapping
    public List<HarvestBatchResponse> getAllHarvestBatches() {
        return harvestBatchService.getAllHarvestBatches();
    }

    @PutMapping("/{id}")
    public HarvestBatchResponse updateHarvestBatch(@PathVariable Long id, @Valid @RequestBody HarvestBatchRequest request) {
        return harvestBatchService.updateHarvestBatch(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHarvestBatch(@PathVariable Long id) {
        harvestBatchService.deleteHarvestBatch(id);
        return ResponseEntity.noContent().build();
    }
}
