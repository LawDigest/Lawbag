package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.PartyPromiseDto;
import com.everyones.lawmaking.common.dto.response.PaginationResponse;
import com.everyones.lawmaking.common.dto.response.PartyPromiseResponse;
import com.everyones.lawmaking.repository.PartyPromiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyPromiseService {

    private final PartyPromiseRepository partyPromiseRepository;

    public PartyPromiseResponse getPartyPromise(long partyId, Pageable pageable) {
        var partyPromiseList = partyPromiseRepository.findPartyPromiseByPartyId(partyId,pageable);
        var pagination = PaginationResponse.from(partyPromiseList);
        var partyPromiseResponse = partyPromiseList.stream().map((PartyPromiseDto::from)).toList();

        return PartyPromiseResponse.of(partyPromiseResponse,pagination);
    }
}
