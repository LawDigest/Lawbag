package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.DistrictCandidateListDto;
import com.everyones.lawmaking.common.dto.response.*;
import com.everyones.lawmaking.global.error.CandidateException;
import com.everyones.lawmaking.global.error.CustomException;
import com.everyones.lawmaking.global.error.ErrorCode;
import com.everyones.lawmaking.repository.DistrictCandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.everyones.lawmaking.global.ResponseCode.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DistrictCandidateService {

    private final DistrictCandidateRepository districtCandidateRepository;
    private static final String CANDIDATE_ID_KEY_STRING = "candidateId";

    public DistrictCandidateDetailResponse getDistrictCandidateDetail(long candidateId ) {
        var districtCandidate = districtCandidateRepository.findDistrictCandidateById(candidateId)
                .orElseThrow(() -> new CandidateException.CandidateNotFound(Map.of(CANDIDATE_ID_KEY_STRING, Long.toString(candidateId))));
        return DistrictCandidateDetailResponse.from(districtCandidate);
    }
    public DistrictCandidateListResponse getDistrictCandidateList(long districtId, Pageable pageable) {
        var districtCandidateList = districtCandidateRepository.findDistrictCandidateByDistrictId(districtId, pageable);
        var pagination = PaginationResponse.from(districtCandidateList);

        var proportionalCandidateResponse = districtCandidateList.stream().map(DistrictCandidateListDto::from).toList();

        return DistrictCandidateListResponse.of(proportionalCandidateResponse,pagination);
    }



}
