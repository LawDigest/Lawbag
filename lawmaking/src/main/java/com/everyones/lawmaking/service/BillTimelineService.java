package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.BillTimelineResponse;
import com.everyones.lawmaking.repository.BillTimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BillTimelineService {
    private final BillTimelineRepository billTimelineRepository;


    public BillTimelineResponse getTimeline(LocalDate proposeDate) {
        var billTimeLines = billTimelineRepository.findBillTimelineByStatusUpdateDate(proposeDate);
        return BillTimelineResponse.of(proposeDate, billTimeLines);
    }
}
