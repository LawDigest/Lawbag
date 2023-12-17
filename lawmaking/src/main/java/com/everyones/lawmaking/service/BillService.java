package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.common.dto.BillSearchDto;
import com.everyones.lawmaking.common.dto.response.MainFeedBillResponse;
import com.everyones.lawmaking.common.dto.response.PaginationResponse;
import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.repository.BillProposerRepository;
import com.everyones.lawmaking.repository.BillRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.everyones.lawmaking.domain.entity.QCongressman.congressman;

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
        bills = setPartyInBillDto(bills);

        var paginationResponse = PaginationResponse.builder().pageNumber(page).isLastPage(false).build();
        return MainFeedBillResponse.builder()
                .Bills(bills)
                .paginationResponse(paginationResponse)
                .build();
    }

    public MainFeedBillResponse getNext3BillsWithStage(int page, Pageable pageable, String stage) {
        var bills = billRepository.findNextThreeBillsWithStage(pageable, stage);
        bills = setPartyInBillDto(bills);
        var paginationResponse = PaginationResponse.builder().pageNumber(page).isLastPage(false).build();
        return MainFeedBillResponse.builder()
                .Bills(bills)
                .paginationResponse(paginationResponse)
                .build();
    }

    public Object getBillWtihDeatail(String billId) {
        var bill = billRepository.findBillDetailByBillId(billId);
        var publicIdsAndProposersAndParty = billProposerRepository.findPartyByBill(billId);

        List<String> publicProposerIds = publicIdsAndProposersAndParty.stream()
                .map(congressman -> congressman[0])
                .collect(Collectors.toList());

        List<String> publicProposers = publicIdsAndProposersAndParty.stream()
                .map(congressman -> congressman[1].length() > 3 ? congressman[1].substring(0, 3) : congressman[1])
                .collect(Collectors.toList());

        var proposerPartyCountList = bill.getProposerPartyCountList();
        var proposerPartyIdList = bill.getProposerPartyIdList();
        var publicIdsAndProposersAndPartyCount = publicIdsAndProposersAndParty.size();
        for (int i = 0; i < publicIdsAndProposersAndPartyCount; i++) {
            String partyName = publicIdsAndProposersAndParty.get(i)[2];
            int partyId = Integer.parseInt(publicIdsAndProposersAndParty.get(i)[3]);

            boolean partyExists = false;
            for (Map<String, Object> partyMap : proposerPartyCountList) {
                if (partyMap.get("name").equals(partyName)) {
                    partyMap.put("count", (int) partyMap.get("count") + 1);
                    partyExists = true;
                    break;
                }
            }
            if (!partyExists) {
                Map<String, Object> newPartyMap = new ConcurrentHashMap<>();
                newPartyMap.put("name", partyName);
                newPartyMap.put("count", 1);
                proposerPartyCountList.add(newPartyMap);
                Map<String, Object> newPartyIdMap = new ConcurrentHashMap<>();
                newPartyIdMap.put("name", partyName);
                newPartyIdMap.put("party_id", partyId);
                proposerPartyIdList.add(newPartyIdMap);
            }

        }


        bill.setPublicProposerList(publicProposers);
        bill.setPublicProposerIdList(publicProposerIds);
        bill.setProposerPartyCountList(proposerPartyCountList);
        bill.setProposerPartyIdList(proposerPartyIdList);

        return bill;
    }


    public List<BillDto> setPartyInBillDto(List<BillDto> bills) {
        var billIds = bills.stream()
                .map(BillDto::getBillId)
                .collect(Collectors.toList());
        var partyList = billProposerRepository.findPartyByBills(billIds);

        Map<String, List<Long>> partyIdMap = partyList.stream()
                .collect(Collectors.groupingBy(row -> (String) row[0],
                        Collectors.mapping(row -> (Long) row[1], Collectors.toList())));
        Map<String, List<String>> partyNameMap =
                partyList.stream()
                        .collect(Collectors.groupingBy(row -> (String) row[0],
                                Collectors.mapping(row -> (String) row[2], Collectors.toList())));
        bills.stream().forEach(billDto -> {
            String billId = billDto.getBillId();
            List<Long> partyIdList = sortByFrequency(partyIdMap.getOrDefault(billId, Collections.emptyList()));
            List<String> partyNameList = sortByFrequency(partyNameMap.getOrDefault(billId, Collections.emptyList()));
            billDto.setPartyIdList(partyIdList);
            billDto.setPartyList(partyNameList);});
        return bills;
    }

    // 리스트 중복제거 정렬 함수
    private static <T> List<T> sortByFrequency(List<T> list) {
        return list.stream()
                .distinct()
                .sorted((a, b) -> {
                    int countA = (int) list.stream().filter(e -> e.equals(a)).count();
                    int countB = (int) list.stream().filter(e -> e.equals(b)).count();
                    return Integer.compare(countB, countA);
                })
                .collect(Collectors.toList());
    }

    //검색 기능 초안
    @Autowired
    private BillRepository billsearchRepository;

    public List<BillSearchDto> searchBillsBySummary(String keyword) {
        return billsearchRepository.findBySummaryContaining(keyword).stream()
                .map(bill -> new BillSearchDto(bill.getId(), bill.getSummary()))
                .collect(Collectors.toList());
    }

}
