//package com.everyones.lawmaking.service;
//
//import com.everyones.lawmaking.common.dto.BillDto;
//import com.everyones.lawmaking.common.dto.response.*;
//import com.everyones.lawmaking.repository.BillRepository;
//import com.everyones.lawmaking.repository.PartyRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class PartyService {
//    private final PartyRepository partyRepository;
//    private final BillRepository billRepository;
//    private final BillService billService;
//
//    public PartyDetailResponse getPartyDetail(int page, Pageable pageable, long partyId, String stage) {
//        PartyDetailDto partyDetails = partyRepository.findPartyDetailById(partyId);
//        List<BillDto> bills = null;
//        if (stage.equals("represent")) {
//            bills = getPartyDetailWithRepresentBills(pageable, partyId);
//        } else {
//            bills = getPartyDetailWithPublicBills(pageable, partyId);
//        }
//
//
//        return PartyDetailResponse.builder()
//                .partyDetail(partyDetails)
//                .bills(bills)
//                .paginationResponse(PaginationResponse.builder()
//                        .pageNumber(page)
//                        .isLastPage(false)
//                        .build())
//                .build();
//    }
//    public List<BillDto> getPartyDetailWithRepresentBills(Pageable pageable, long partyId) {
//        var bills = billRepository.findNextThreeBillsWithPartyAndRepresentive(pageable, partyId);
//        billService.setPartyInBillDto(bills);
//        return bills;
//    }
//
//    public List<BillDto> getPartyDetailWithPublicBills(Pageable pageable, long partyId) {
//        var billIds = billRepository.findBillIdsBillsWithPartyAndPublic(pageable, partyId);
//        var bills = billRepository.findNextThreeBillsWithBills(billIds);
//        billService.setPartyInBillDto(bills);
//        return bills;
//    }
//
//
//
//
//
//}
