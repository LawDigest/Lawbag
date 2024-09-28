package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.projection.CommitteeBillDto;
import com.everyones.lawmaking.repository.BillTimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillTimelineService {
    private final BillTimelineRepository billTimelineRepository;


//    public BillTimelineResponse getTimeline(LocalDate proposeDate) {
//        var billTimeLines = billTimelineRepository.findBillTimelineByStatusUpdateDate(proposeDate);
//        return BillTimelineResponse.of(proposeDate, billTimeLines);
//    }

    public List<String> findPromulgationBillIds(LocalDate localDate) {
        return billTimelineRepository.findBillTimelineBetweenProposeDateAndStage(localDate, "공포");
    }

    public List<String> findPlenaryBillIds(LocalDate localDate) {
        return billTimelineRepository.findBillTimelineBetweenProposeDateAndStage(localDate, "본회의 심의");
    }

    public List<CommitteeBillDto> findCommitteesWithMultipleBillIds(LocalDate proposeDate) {
        var tupleList = billTimelineRepository.findCommitteesWithMultipleBills(proposeDate);
        return tupleList.stream()
                .map(tuple -> {
                    String committee = tuple.get("committee", String.class);
                    String billIdsConcat = tuple.get("billIds", String.class);
                    List<String> billIds = Arrays.asList(billIdsConcat.split(","));
                    return CommitteeBillDto.of(committee, billIds);
                }).toList();
    }
}
