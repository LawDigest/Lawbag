package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.DistrictCandidateListDto;
import com.everyones.lawmaking.common.dto.ProportionalCandidateListDto;
import com.everyones.lawmaking.common.dto.response.*;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.repository.DistrictCandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.everyones.lawmaking.global.ResponseCode.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DistrictCandidateService {

    private final DistrictCandidateRepository districtCandidateRepository;

    public DistrictCandidateDetailResponse getDistrictCandidateDetail(long candidateId ) {
            var candidateDetail = districtCandidateRepository.findDistrictCandidateById(candidateId)
                    .orElseThrow(() -> new CustomException(INTERNAL_SERVER_ERROR));
        return DistrictCandidateDetailResponse.from(candidateDetail);
    }
    public DistrictCandidateListResponse getDistrictCandidateList(long districtId, Pageable pageable) {
        var districtCandidateList = districtCandidateRepository.findDistrictCandidateByDistrictId(districtId, pageable);
        var pagination = PaginationResponse.fromSlice(districtCandidateList);

        var proportionalCandidateResponse = districtCandidateList.stream().map(DistrictCandidateListDto::from).toList();

        return DistrictCandidateListResponse.of(proportionalCandidateResponse,pagination);
    }



}
