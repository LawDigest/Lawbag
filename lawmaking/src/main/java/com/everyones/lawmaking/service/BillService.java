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
        var billIds = bills.stream()
                .map(BillDto::getBillId)
                .collect(Collectors.toList());

        // billid와 같은 공동발의자가져오고 묶어주기
        var publicProposers = billProposerRepository.findCongressmanNamesByBillIdList(billIds).stream()
                .collect(Collectors.groupingBy(row -> (String) row[0],
                        Collectors.mapping(row -> (String) row[1], Collectors.toList())));

        // publicProposers에서 가져온 공동발의자 명단 BillDto에 넣기
        bills.forEach(billDto -> {
            String billId = billDto.getBillId();
            List<String> proposers = publicProposers.getOrDefault(billId, Collections.emptyList());
            billDto.setPublicProposer(proposers);
        });
        var paginationResponse = PaginationResponse.builder().pageNumber(page).build();
        var mainfeedBillResponse = MainFeedBillResponse.of(paginationResponse, bills);
        return mainfeedBillResponse;
    }

}
