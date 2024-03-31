package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.PartyPromiseResponse;
import com.everyones.lawmaking.repository.PartyPromiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyPromiseService {

    private final PartyPromiseRepository partyPromiseRepository;

    public List<PartyPromiseResponse> getPartyPromise(long partyId, Pageable pageable) {
        var partyPromiseList = partyPromiseRepository.findPartyPromiseByPartyId(partyId,pageable);

        return partyPromiseList.stream().map(PartyPromiseResponse::from).collect(Collectors.toList());
    }
}
