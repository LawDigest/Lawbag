package com.everyones.lawmaking.facade;

import com.everyones.lawmaking.common.dto.CongressmanBillDto;
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
    private final PartyService partySerivce;
    private final BillService billService;
    private final CongressmanService congressmanService;


    // Congressman 대표 발의 가져오기
    public List<CongressmanBillDto> getBillsFromRepresentativeProposer(String congressmanId, Pageable pageable) {
        return billService.getBillInfoFromRepresentativeProposer(congressmanId, pageable);
    }

    public List<CongressmanBillDto> getBillsFromPublicProposer(String congressmanId, Pageable pageable) {

        return billService.getBillInfoFromPublicProposer(congressmanId, pageable);
    }




}
