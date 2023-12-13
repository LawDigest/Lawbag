package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.common.dto.response.MainFeedBillResponse;
import com.everyones.lawmaking.common.dto.response.PaginationResponse;
import com.everyones.lawmaking.repository.BillProposerRepository;
import com.everyones.lawmaking.repository.BillRepository;
import com.everyones.lawmaking.repository.BillRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class BillService {
    private final BillRepository billRepository;
    private final BillProposerRepository billProposerRepository;

    // TODO: Query 1개로 묶는 것을 목표 + 로직이 최신 법안 3개 가져오는 형태 추후 수정 + summary에 개행문자 제거 소요 있음
    public MainFeedBillResponse getNext3Bills(int page, Pageable pageable) {

        // 메인피드에서 Bill 정보 페이지네이션으로 가져오기
        var bills = billRepository.findNextThreeBills(pageable);
        var paginationResponse = PaginationResponse.builder().pageNumber(page).isLastPage(false).build();
        return MainFeedBillResponse.builder()
                .Bills(bills)
                .paginationResponse(paginationResponse)
                .build();
    }

    public MainFeedBillResponse getNext3BillsWithStage(int page, Pageable pageable, String stage) {
        var bills = billRepository.findNextThreeBillsWithStage(pageable, stage);
        var paginationResponse = PaginationResponse.builder().pageNumber(page).isLastPage(false).build();
        return MainFeedBillResponse.builder()
                .Bills(bills)
                .paginationResponse(paginationResponse)
                .build();
    }

}
