package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.PartyDetailDto;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;

    public PartyDetailDto getPartyById(long partyId) {

        return partyRepository.findPartyDetailById(partyId)
                .orElseThrow(() -> new CustomException(ResponseCode.BAD_REQUEST));
    }


}
