package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.common.dto.BillDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BillRepositoryCustom {
    Slice<BillDto> findNextThreeBills(@Param("startDate") LocalDateTime startDate, Pageable pageable);
}
