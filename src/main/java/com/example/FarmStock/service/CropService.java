package com.example.FarmStock.service;

import com.example.FarmStock.dto.CropRequest;
import com.example.FarmStock.dto.CropResponse;
import com.example.FarmStock.dto.RevenueSummaryResponse;
import com.example.FarmStock.dto.StockLevelResponse;
import com.example.FarmStock.entity.Crop;
import com.example.FarmStock.repository.CropRepository;
import com.example.FarmStock.repository.HarvestBatchRepository;
import com.example.FarmStock.repository.SaleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CropService {

    private final CropRepository cropRepository;
    private final HarvestBatchRepository harvestBatchRepository;
    private final SaleRepository saleRepository;

    public CropService(CropRepository cropRepository, HarvestBatchRepository harvestBatchRepository, SaleRepository saleRepository) {
        this.cropRepository = cropRepository;
        this.harvestBatchRepository = harvestBatchRepository;
        this.saleRepository = saleRepository;
    }

    @Transactional
    public CropResponse createCrop(CropRequest request) {
        String normalizedName = request.name().trim();
        if (normalizedName.isBlank()) {
            throw new IllegalArgumentException("Crop name cannot be blank.");
        }
        if (cropRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new IllegalArgumentException("Crop with name '" + normalizedName + "' already exists.");
        }

        Crop crop = new Crop(normalizedName);
        return CropResponse.from(cropRepository.save(crop));
    }

    @Transactional
    public CropResponse updateCrop(Long cropId, CropRequest request) {
        Crop crop = findCrop(cropId);
        String normalizedName = request.name().trim();
        if (normalizedName.isBlank()) {
            throw new IllegalArgumentException("Crop name cannot be blank.");
        }
        if (!crop.getName().equalsIgnoreCase(normalizedName) && cropRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new IllegalArgumentException("Crop with name '" + normalizedName + "' already exists.");
        }

        crop.setName(normalizedName);
        return CropResponse.from(cropRepository.save(crop));
    }

    @Transactional
    public void deleteCrop(Long cropId) {
        Crop crop = findCrop(cropId);
        cropRepository.delete(crop);
    }

    public List<CropResponse> getAllCrops() {
        return cropRepository.findAll().stream().map(CropResponse::from).toList();
    }

    public CropResponse getCropById(Long cropId) {
        return CropResponse.from(findCrop(cropId));
    }

    public List<StockLevelResponse> getAllStockLevels() {
        return cropRepository.findAll().stream().map(this::toStockLevel).toList();
    }

    public StockLevelResponse getStockLevelByCropId(Long cropId) {
        return toStockLevel(findCrop(cropId));
    }

    public RevenueSummaryResponse getRevenueByCrop(Long cropId, LocalDate startDate, LocalDate endDate) {
        Crop crop = findCrop(cropId);
        BigDecimal totalRevenue = saleRepository.sumTotalPriceByCropIdAndDateRange(cropId, startDate, endDate);
        return new RevenueSummaryResponse(crop.getId(), crop.getName(), totalRevenue == null ? BigDecimal.ZERO : totalRevenue);
    }

    public List<RevenueSummaryResponse> getRevenueByCropRange(LocalDate startDate, LocalDate endDate) {
        return saleRepository.findRevenueGroupedByCropAndDateRange(startDate, endDate)
            .stream()
            .map(RevenueSummaryResponse::fromRow)
            .toList();
    }

    public int calculateAvailableStock(Long cropId) {
        Crop crop = findCrop(cropId);
        return calculateAvailableStock(crop);
    }

    public int calculateAvailableStock(Crop crop) {
        int harvested = harvestBatchRepository.sumHarvestedQuantityByCropId(crop.getId());
        int sold = saleRepository.sumSoldQuantityByCropId(crop.getId());
        return harvested - sold;
    }

    private Crop findCrop(Long cropId) {
        return cropRepository.findById(cropId)
            .orElseThrow(() -> new EntityNotFoundException("Crop not found with id: " + cropId));
    }

    private StockLevelResponse toStockLevel(Crop crop) {
        return new StockLevelResponse(crop.getId(), crop.getName(), calculateAvailableStock(crop));
    }
}
