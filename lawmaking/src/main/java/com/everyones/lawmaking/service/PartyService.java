package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.SearchPartyDto;
import com.everyones.lawmaking.common.dto.response.FollowingPartyResponse;
import com.everyones.lawmaking.common.dto.response.PartyDetailResponse;
import com.everyones.lawmaking.common.dto.response.SearchResponse;
import com.everyones.lawmaking.domain.entity.Congressman;
import com.everyones.lawmaking.domain.entity.Party;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;

    public Party findParty(long partyId) {
        return partyRepository.findById(partyId)
                .orElseThrow(() -> new CustomException(ResponseCode.BAD_REQUEST));
    }

    public PartyDetailResponse getPartyById(long partyId) {

        return partyRepository.findPartyDetailById(partyId)
                .orElseThrow(() -> new CustomException(ResponseCode.BAD_REQUEST));
    }

    public List<FollowingPartyResponse> getFollowingParty(long userId) {
        var parties = partyRepository.findFollowingPartyByUserId(userId);

        return parties.stream()
                .map(FollowingPartyResponse::from)
                .toList();
    }

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


}
