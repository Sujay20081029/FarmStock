package com.example.FarmStock.repository;

import com.example.FarmStock.entity.HarvestBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HarvestBatchRepository extends JpaRepository<HarvestBatch, Long> {

    @Query("select coalesce(sum(h.quantity), 0) from HarvestBatch h where h.crop.id = :cropId")
    int sumHarvestedQuantityByCropId(@Param("cropId") Long cropId);
}
