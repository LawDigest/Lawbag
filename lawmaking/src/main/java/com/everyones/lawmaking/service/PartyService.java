package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.PartyDetailDto;
import com.everyones.lawmaking.common.dto.response.PartyDetailResponse;
import com.everyones.lawmaking.repository.BillRepository;
import com.everyones.lawmaking.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final BillRepository billRepository;

    public PartyDetailDto getPartyDetail(long partyId) {
        return partyRepository.findPartyDetailById(partyId);
    }
    public PartyDetailResponse getPartyDetailWithRepresentBills(long partyId) {
        var partyDetailDto = getPartyDetail(partyId);
        return null;
    }

    public PartyDetailResponse getPartyDetailWithPublicBills(long partyId) {
        var partyDetailDto = getPartyDetail(partyId);
        return null;
    }





}
