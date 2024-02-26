package com.everyones.lawmaking.facade;

import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.common.dto.CongressmanDto;
import com.everyones.lawmaking.common.dto.response.PartyDetailDto;
import com.everyones.lawmaking.service.BillService;
import com.everyones.lawmaking.service.CongressmanService;
import com.everyones.lawmaking.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class Facade {
    private final PartyService partyService;
    private final BillService billService;
    private final CongressmanService congressmanService;

    public List<BillDto> getBillsFromMainFeed(Pageable pageable) {
        return billService.getBillsByDefault(pageable);
    }

    public List<BillDto> getBillsByStage(Pageable pageable, String stage) {
        return billService.getBillsByStage(pageable, stage);
    }

    public BillDto getBillByBillId(String billId) {
        return billService.getBillWithDetail(billId);
    }

    // Congressman 대표 발의 가져오기
    public List<BillDto> getBillsFromRepresentativeProposer(String congressmanId, Pageable pageable) {
        return billService.getBillInfoFromRepresentativeProposer(congressmanId, pageable);
    }

    public List<BillDto> getBillsFromPublicProposer(String congressmanId, Pageable pageable) {
        return billService.getBillInfoFromPublicProposer(congressmanId, pageable);
    }

    public CongressmanDto getCongressman(String congressmanId) {
        return congressmanService.getCongressman(congressmanId);
    }

    public PartyDetailDto getPartyById(long partyId) {
        return partyService.getPartyById(partyId);
    }

    public List<BillDto> getRepresentativeBillsByParty(Pageable pageable, long partyId) {
        return billService.getRepresentativeBillsByParty(pageable, partyId);
    }

    public List<BillDto> getPublicBillsByParty(Pageable pageable, long partyId) {
        return billService.getPublicBillsByParty(pageable, partyId);
    }






}
