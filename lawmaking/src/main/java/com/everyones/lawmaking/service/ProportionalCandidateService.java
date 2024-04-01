package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.ProportionalCandidateListDto;
import com.everyones.lawmaking.common.dto.response.PaginationResponse;
import com.everyones.lawmaking.common.dto.response.ProportionalCandidateListResponse;
import com.everyones.lawmaking.repository.ProportionalCandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProportionalCandidateService {

    private final ProportionalCandidateRepository proportionalCandidateRepository;

    public ProportionalCandidateListResponse getProportionalCandidateList(long partyId, Pageable pageable) {
        var proportionalCandidateList = proportionalCandidateRepository.findProportionalCandidateByPartyId(partyId,pageable);
        var pagination = PaginationResponse.fromSlice(proportionalCandidateList);
        var proportionalCandidateResponse = proportionalCandidateList.stream().map((ProportionalCandidateListDto::from)).toList();

        return ProportionalCandidateListResponse.of(proportionalCandidateResponse,pagination);
    }
}