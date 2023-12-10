package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.domain.entity.BillProposer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillProposerRepository extends JpaRepository<BillProposer, Long> {
    List<BillProposer> findByBill(Bill bill);
}
