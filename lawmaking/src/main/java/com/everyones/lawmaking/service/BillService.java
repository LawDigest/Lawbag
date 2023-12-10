package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.common.dto.response.GetBillWithMainFeedRes;
import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BillService {
    private final BillRepository billRepository;
    private static int size = 3;

    public List<BillDto> getNext3Bills(String lastBillId, LocalDateTime lastProposeDate) {
        return billRepository.findNext3Bills(lastBillId, lastProposeDate);
    }

}
