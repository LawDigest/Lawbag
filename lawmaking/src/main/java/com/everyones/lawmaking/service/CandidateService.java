package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.SearchBillDto;
import com.everyones.lawmaking.common.dto.SearchCandidateDto;
import com.everyones.lawmaking.common.dto.response.PaginationResponse;
import com.everyones.lawmaking.common.dto.response.SearchDataResponse;
import com.everyones.lawmaking.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class CandidateService {
    private final CandidateRepository candidateRepository;
    public SearchDataResponse searchCandidate(String searchWord, Pageable pageable) {
        var candidateSlice = candidateRepository.findCandidateByKeyword(pageable,searchWord);
        var paginationResponse = PaginationResponse.from(candidateSlice);
        var candidateList = candidateSlice.stream().map(SearchCandidateDto::from).toList();

        return SearchDataResponse.builder()
                .searchResponse(candidateList)
                .paginationResponse(paginationResponse)
                .build();


    }

}
