package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.common.dto.BillDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class BillRepositoryImpl implements BillRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<BillDto> findNextThreeBills(LocalDateTime startDate, Pageable pageable) {
        return null;
    }



}
