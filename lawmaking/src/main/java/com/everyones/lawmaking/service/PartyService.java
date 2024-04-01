package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.ProportionalPartyImageListDto;
import com.everyones.lawmaking.common.dto.SearchPartyDto;
import com.everyones.lawmaking.common.dto.response.*;
import com.everyones.lawmaking.domain.entity.Party;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {
    private final PartyRepository partyRepository;

    public Party findParty(long partyId) {
        return partyRepository.findById(partyId)
                .orElseThrow(() -> new CustomException(ResponseCode.BAD_REQUEST));
    }

    public PartyDetailResponse getPartyById(long partyId) {
        var party = partyRepository.findPartyDetailById(partyId)
                .orElseThrow(() -> new CustomException(ResponseCode.BAD_REQUEST));
        return PartyDetailResponse.from(party);
    }

    public List<FollowingPartyResponse> getFollowingParty(long userId) {
        var parties = partyRepository.findFollowingPartyByUserId(userId);

        return parties.stream()
                .map(FollowingPartyResponse::from)
                .toList();
    }

    @Transactional
    public void updatePartyFollowCount(Party party, boolean followChecked) {
        var followCount = followChecked ? party.getFollowCount() + 1 : party.getFollowCount() - 1;
        party.setFollowCount(followCount);
        partyRepository.save(party);
    }

    // 검색 파티
    public List<SearchResponse> searchParty(String searchWord) {
        var searchPartyList= partyRepository.findBySearchWord(searchWord);
        return searchPartyList.stream()
                .map(SearchPartyDto::from)
                .collect(Collectors.toList());
    }

    public ProportionalPartyImageListResponse getProportionalPartyImageList(Pageable pageable) {
        var proportionalPartyList = partyRepository.findProportionalParty(pageable);
        var pagination = PaginationResponse.fromSlice(proportionalPartyList);
        var proportionalPartyLogoResponse = proportionalPartyList.stream().map((ProportionalPartyImageListDto::from)).toList();

        return ProportionalPartyImageListResponse.of(proportionalPartyLogoResponse,pagination);
    }


}
