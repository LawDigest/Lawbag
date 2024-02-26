package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.BillProposer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillProposerRepository extends JpaRepository<BillProposer, Long> {

}
