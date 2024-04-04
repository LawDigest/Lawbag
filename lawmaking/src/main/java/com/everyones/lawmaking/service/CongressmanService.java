package com.everyones.lawmaking.service;


import com.everyones.lawmaking.common.dto.PartyCongressmanDto;
import com.everyones.lawmaking.common.dto.SearchCongressmanDto;
import com.everyones.lawmaking.common.dto.response.*;
import com.everyones.lawmaking.domain.entity.Congressman;
import com.everyones.lawmaking.repository.CongressmanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CongressmanService {
    private final CongressmanRepository congressmanRepository;

    public Congressman findById(String congressmanId) {
        return congressmanRepository.findByIdWithParty(congressmanId)
                .orElseThrow(() -> new EntityNotFoundException("해당 의원이 존재하지 않습니다."));
    }

    public CongressmanResponse getCongressman(String congressmanId) {
        var congressman = findById(congressmanId);
        return CongressmanResponse.from(congressman);
    }

    public PartyCongressmanResponse getPartyCongressman(long partyId, Pageable pageable) {
        var congressman = congressmanRepository.findCongressmanById(partyId, pageable);
        var pagination = PaginationResponse.fromSlice(congressman);
        var congresssmanList = congressman.stream()
                .map(PartyCongressmanDto::from)
                .toList();
        return PartyCongressmanResponse.of(congresssmanList, pagination);
    }

    @Transactional
    public void updateCongressmanLikeCount(Congressman congressman, boolean likeChecked) {

        var likeCount = likeChecked ? congressman.getLikeCount() + 1 : congressman.getLikeCount() - 1;
        congressman.setLikeCount(likeCount);
        congressmanRepository.save(congressman);
    }

    public List<LikingCongressmanResponse> getLikingCongressman(long userId){
        var likingCongressman = congressmanRepository.findLikingCongressmanByUserId(userId);
        return likingCongressman.stream()
                .map(LikingCongressmanResponse::from)
                .toList();

    }

    public SearchDataResponse searchCongressman(String searchWord, Pageable pageable) {
        var congressmanList = congressmanRepository.findBySearchWord(searchWord, pageable);
        var pagination = PaginationResponse.fromSlice(congressmanList);
        var searchResponse =  congressmanList.stream()
                .map(SearchCongressmanDto::from)
                .collect(Collectors.toList());

        return SearchDataResponse.builder()
                .searchResponse(searchResponse)
                .paginationResponse(pagination)
                .build();
    }

}

