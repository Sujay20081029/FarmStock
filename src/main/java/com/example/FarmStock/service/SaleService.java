package com.example.FarmStock.service;

import com.example.FarmStock.dto.SaleRequest;
import com.example.FarmStock.dto.SaleResponse;
import com.example.FarmStock.entity.Crop;
import com.example.FarmStock.entity.Sale;
import com.example.FarmStock.repository.CropRepository;
import com.example.FarmStock.repository.SaleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SaleService {

    private final SaleRepository saleRepository;
    private final CropRepository cropRepository;
    private final CropService cropService;

    public SaleService(SaleRepository saleRepository, CropRepository cropRepository, CropService cropService) {
        this.saleRepository = saleRepository;
        this.cropRepository = cropRepository;
        this.cropService = cropService;
    }

    @Transactional
    public SaleResponse createSale(SaleRequest request) {
        Crop crop = cropRepository.findById(request.cropId())
            .orElseThrow(() -> new EntityNotFoundException("Crop not found with id: " + request.cropId()));

        int availableStock = cropService.calculateAvailableStock(crop);
        if (request.quantity() > availableStock) {
            throw new IllegalArgumentException(
                "Sale quantity exceeds available stock for crop '" + crop.getName() + "'. Available stock: " + availableStock
            );
        }

        Sale sale = new Sale(crop, request.quantity(), request.saleDate(), request.totalPrice());
        return SaleResponse.from(saleRepository.save(sale));
    }

    @Transactional
    public SaleResponse updateSale(Long saleId, SaleRequest request) {
        Sale sale = saleRepository.findById(saleId)
            .orElseThrow(() -> new EntityNotFoundException("Sale not found with id: " + saleId));

        Crop crop = cropRepository.findById(request.cropId())
            .orElseThrow(() -> new EntityNotFoundException("Crop not found with id: " + request.cropId()));

        int totalSold = saleRepository.sumSoldQuantityByCropId(crop.getId());
        int totalHarvested = cropService.calculateAvailableStock(crop) + totalSold;
        int availableWithoutCurrent = totalHarvested - (totalSold - sale.getQuantity());

        if (request.quantity() > availableWithoutCurrent) {
            throw new IllegalArgumentException(
                "Sale quantity exceeds available stock for crop '" + crop.getName() + "'. Available stock: " + availableWithoutCurrent
            );
        }

        sale.setCrop(crop);
        sale.setQuantity(request.quantity());
        sale.setSaleDate(request.saleDate());
        sale.setTotalPrice(request.totalPrice());
        return SaleResponse.from(saleRepository.save(sale));
    }

    @Transactional
    public void deleteSale(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
            .orElseThrow(() -> new EntityNotFoundException("Sale not found with id: " + saleId));
        saleRepository.delete(sale);
    }

    public List<SaleResponse> getAllSales() {
        return saleRepository.findAll().stream().map(SaleResponse::from).toList();
    }

    public List<SaleResponse> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        return saleRepository.findAll().stream()
            .filter(sale -> !sale.getSaleDate().isBefore(startDate) && !sale.getSaleDate().isAfter(endDate))
            .map(SaleResponse::from)
            .toList();
    }
}
