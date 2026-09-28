package com.example.FarmStock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.FarmStock.dto.CropRequest;
import com.example.FarmStock.dto.HarvestBatchRequest;
import com.example.FarmStock.dto.SaleRequest;
import com.example.FarmStock.dto.StockLevelResponse;
import com.example.FarmStock.service.CropService;
import com.example.FarmStock.service.HarvestBatchService;
import com.example.FarmStock.service.SaleService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FarmStockServiceIntegrationTest {

    @Autowired
    private CropService cropService;

    @Autowired
    private HarvestBatchService harvestBatchService;

    @Autowired
    private SaleService saleService;

    @Test
    void shouldTrackStockAndRejectOversell() {
        CropRequest cropRequest = new CropRequest("Tomato");
        var crop = cropService.createCrop(cropRequest);

        harvestBatchService.createHarvestBatch(new HarvestBatchRequest(crop.id(), 100, LocalDate.of(2026, 1, 5)));

        StockLevelResponse stock = cropService.getStockLevelByCropId(crop.id());
        assertEquals(100, stock.currentStock());

        saleService.createSale(new SaleRequest(crop.id(), 40, LocalDate.of(2026, 1, 8), new BigDecimal("2.50")));
        stock = cropService.getStockLevelByCropId(crop.id());
        assertEquals(60, stock.currentStock());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> saleService.createSale(new SaleRequest(crop.id(), 70, LocalDate.of(2026, 1, 9), new BigDecimal("2.50")))
        );

        assertEquals("Sale quantity exceeds available stock for crop 'Tomato'. Available stock: 60", exception.getMessage());
    }

    @Test
    void shouldSummarizeRevenueByCropInDateRange() {
        CropRequest cropRequest = new CropRequest("Wheat");
        var crop = cropService.createCrop(cropRequest);

        harvestBatchService.createHarvestBatch(new HarvestBatchRequest(crop.id(), 200, LocalDate.of(2026, 2, 1)));
        saleService.createSale(new SaleRequest(crop.id(), 80, LocalDate.of(2026, 2, 10), new BigDecimal("3.50")));
        saleService.createSale(new SaleRequest(crop.id(), 50, LocalDate.of(2026, 2, 15), new BigDecimal("2.00")));

        var revenue = cropService.getRevenueByCrop(crop.id(), LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28));

        assertEquals(new BigDecimal("380.00"), revenue.totalRevenue());
    }
}
