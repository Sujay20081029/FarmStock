package com.example.FarmStock.service;

import com.example.FarmStock.dto.HarvestBatchRequest;
import com.example.FarmStock.dto.HarvestBatchResponse;
import com.example.FarmStock.entity.Crop;
import com.example.FarmStock.entity.HarvestBatch;
import com.example.FarmStock.repository.CropRepository;
import com.example.FarmStock.repository.HarvestBatchRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HarvestBatchService {

    private final HarvestBatchRepository harvestBatchRepository;
    private final CropRepository cropRepository;

    public HarvestBatchService(HarvestBatchRepository harvestBatchRepository, CropRepository cropRepository) {
        this.harvestBatchRepository = harvestBatchRepository;
        this.cropRepository = cropRepository;
    }

    @Transactional
    public HarvestBatchResponse createHarvestBatch(HarvestBatchRequest request) {
        Crop crop = cropRepository.findById(request.cropId())
            .orElseThrow(() -> new EntityNotFoundException("Crop not found with id: " + request.cropId()));

        HarvestBatch batch = new HarvestBatch(crop, request.quantity(), request.harvestDate());
        return HarvestBatchResponse.from(harvestBatchRepository.save(batch));
    }

    @Transactional
    public HarvestBatchResponse updateHarvestBatch(Long batchId, HarvestBatchRequest request) {
        HarvestBatch batch = harvestBatchRepository.findById(batchId)
            .orElseThrow(() -> new EntityNotFoundException("Harvest batch not found with id: " + batchId));

        Crop crop = cropRepository.findById(request.cropId())
            .orElseThrow(() -> new EntityNotFoundException("Crop not found with id: " + request.cropId()));

        batch.setCrop(crop);
        batch.setQuantity(request.quantity());
        batch.setHarvestDate(request.harvestDate());
        return HarvestBatchResponse.from(harvestBatchRepository.save(batch));
    }

    @Transactional
    public void deleteHarvestBatch(Long batchId) {
        HarvestBatch batch = harvestBatchRepository.findById(batchId)
            .orElseThrow(() -> new EntityNotFoundException("Harvest batch not found with id: " + batchId));
        harvestBatchRepository.delete(batch);
    }

    public List<HarvestBatchResponse> getAllHarvestBatches() {
        return harvestBatchRepository.findAll().stream().map(HarvestBatchResponse::from).toList();
    }
}
