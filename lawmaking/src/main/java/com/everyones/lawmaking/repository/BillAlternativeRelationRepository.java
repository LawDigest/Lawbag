package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.BillAlternativeRelation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BillAlternativeRelationRepository extends JpaRepository<BillAlternativeRelation, Long> {

    boolean existsByBill_IdAndIncludedBill_Id(String billId, String alternateBillId);
}
