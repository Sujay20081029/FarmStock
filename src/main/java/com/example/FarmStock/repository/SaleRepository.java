package com.example.FarmStock.repository;

import com.example.FarmStock.entity.Sale;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("select coalesce(sum(s.quantity), 0) from Sale s where s.crop.id = :cropId")
    int sumSoldQuantityByCropId(@Param("cropId") Long cropId);

    @Query("select coalesce(sum(s.totalPrice * s.quantity), 0) from Sale s where s.crop.id = :cropId and s.saleDate between :startDate and :endDate")
    BigDecimal sumTotalPriceByCropIdAndDateRange(@Param("cropId") Long cropId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select s.crop.id, s.crop.name, coalesce(sum(s.totalPrice * s.quantity), 0) from Sale s where s.saleDate between :startDate and :endDate group by s.crop.id, s.crop.name")
    List<Object[]> findRevenueGroupedByCropAndDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
