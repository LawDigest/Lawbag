package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.MainFeedBillResponse;
import com.everyones.lawmaking.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class BillService {
    private final BillRepository billRepository;
    private static int size = 3;

    public MainFeedBillResponse getNext3Bills(String lastBillId, LocalDateTime lastProposeDate) {
        var bills = billRepository.getNext3Bills(lastBillId, lastProposeDate);
        return MainFeedBillResponse.of(bills);
    }

}
