package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.FollowingPartyResponse;
import com.everyones.lawmaking.common.dto.response.PartyDetailResponse;
import com.everyones.lawmaking.domain.entity.Congressman;
import com.everyones.lawmaking.domain.entity.Party;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
