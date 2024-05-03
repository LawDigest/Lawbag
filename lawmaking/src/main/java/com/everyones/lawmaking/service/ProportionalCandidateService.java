package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.ProportionalCandidateListDto;
import com.everyones.lawmaking.common.dto.response.PaginationResponse;
import com.everyones.lawmaking.common.dto.response.ProportionalCandidateDetailResponse;
import com.everyones.lawmaking.common.dto.response.ProportionalCandidateListResponse;
import com.everyones.lawmaking.global.error.CandidateException;
import com.everyones.lawmaking.global.error.CustomException;
import com.everyones.lawmaking.global.error.ErrorCode;
import com.everyones.lawmaking.repository.ProportionalCandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.everyones.lawmaking.global.ResponseCode.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProportionalCandidateService {

    private final ProportionalCandidateRepository proportionalCandidateRepository;

    public ProportionalCandidateListResponse getProportionalCandidateList(long partyId, Pageable pageable) {
        var proportionalCandidateList = proportionalCandidateRepository.findProportionalCandidateByPartyId(partyId,pageable);
        var pagination = PaginationResponse.from(proportionalCandidateList);
        var proportionalCandidateResponse = proportionalCandidateList.stream().map((ProportionalCandidateListDto::from)).toList();

        return ProportionalCandidateListResponse.of(proportionalCandidateResponse,pagination);
    }
    public ProportionalCandidateDetailResponse getProportionalCandidateDetail(long candidateId) {
        var candidateDetail = proportionalCandidateRepository.findProportionalCandidateById(candidateId)
                .orElseThrow(() -> new CandidateException.CandidateNotFound(Map.of("candidateId", Long.toString(candidateId))));

        return ProportionalCandidateDetailResponse.from(candidateDetail);
    }

}