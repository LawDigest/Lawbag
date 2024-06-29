package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.BillLikeResponse;
import com.everyones.lawmaking.common.dto.response.BillListResponse;
import com.everyones.lawmaking.common.dto.response.CongressmanLikeResponse;
import com.everyones.lawmaking.common.dto.response.PartyFollowResponse;
import com.everyones.lawmaking.domain.entity.*;
import com.everyones.lawmaking.global.error.LikeException;
import com.everyones.lawmaking.repository.BillLikeRepository;
import com.everyones.lawmaking.repository.CongressmanLikeRepository;
import com.everyones.lawmaking.repository.PartyFollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LikeService {
    private final BillLikeRepository billLikeRepository;
    private final CongressmanLikeRepository congressmanLikeRepository;
    private final PartyFollowRepository partyFollowRepository;

    private static final String UPDATE_PARAMETER = "updateParameter";

    public Boolean getCongressmanLike(String congressmanId, long userId) {
        var congressmanLike = congressmanLikeRepository.findByUserIdAndCongressmanId(userId, congressmanId);
        return congressmanLike.map(CongressManLike::isLikeChecked).orElse(false);
    }
    public Boolean getFollowParty(long partyId, long userId) {
        var partyFollow = partyFollowRepository.findByUserIdAndPartyId(userId,partyId);
        return partyFollow.map(PartyFollow::isFollowChecked).orElse(false);
    }

    public Boolean getBillLikeChecked(String billId, long userId) {
        var billLike = billLikeRepository.findByUserIdAndBillId(userId, billId);
        return billLike.map(BillLike::isLikeChecked).orElse(false);
    }

    public BillLikeResponse likeBill(User user, Bill bill, boolean likeChecked) {
        var billLike = billLikeRepository.findByUserIdAndBillId(user.getId(), bill.getId());
        if (billLike.isPresent()) {
            if (likeChecked) {
                throw new LikeException.UpdateParameterException(Map.of("likeChecked", Boolean.toString(likeChecked)));
            }
            return deleteBillLike(billLike.get());
        }
        if(!likeChecked) {
            throw new LikeException.UpdateParameterException(Map.of("likeChecked", Boolean.toString(likeChecked)));
        }
        return createBillLike(user, bill);
    }


    public CongressmanLikeResponse likeCongressman(User user, Congressman congressman, boolean likeChecked) {
        var congressmanLike = congressmanLikeRepository.findByUserIdAndCongressmanId(user.getId(), congressman.getId());
        if (congressmanLike.isPresent()) {
            if (likeChecked) {
                throw new LikeException.UpdateParameterException(Map.of("likeChecked", Boolean.toString(likeChecked)));
            }
            return deleteCongressmanLike(congressmanLike.get());
        }
        if(!likeChecked) {
            throw new LikeException.UpdateParameterException(Map.of("likeChecked", Boolean.toString(likeChecked)));
        }
        return createCongressmanLike(user, congressman);
    }


    public PartyFollowResponse followParty(User user, Party party, boolean followChecked) {
        var partyFollow = partyFollowRepository.findByUserIdAndPartyId(user.getId(), party.getId());

        return partyFollow.isPresent() ? updatePartyFollow(partyFollow.get(), followChecked) : createPartyFollow(user, party, followChecked);
    }


    private PartyFollowResponse createPartyFollow(User user, Party party, boolean followChecked) {
        isEqual(false, followChecked);
        var partyFollow = PartyFollow.builder()
                .party(party)
                .user(user)
                .followChecked(followChecked)
                .build();
        partyFollowRepository.save(partyFollow);
        return PartyFollowResponse.from(partyFollow);
    }

    private PartyFollowResponse updatePartyFollow(PartyFollow partyFollow, boolean followChecked) {
        isEqual(partyFollow.isFollowChecked(), followChecked);
        partyFollow.setFollowChecked(followChecked);
        partyFollowRepository.save(partyFollow);

        return PartyFollowResponse.from(partyFollow);
    }


    private CongressmanLikeResponse createCongressmanLike(User user, Congressman congressman) {
        var congressmanLike = CongressManLike.builder()
                .congressman(congressman)
                .user(user)
                .build();
        congressmanLikeRepository.save(congressmanLike);
        return CongressmanLikeResponse.from(true);
    }

    private CongressmanLikeResponse deleteCongressmanLike(CongressManLike congressmanLike) {
        congressmanLikeRepository.deleteById(congressmanLike.getId());
    }

    private BillLikeResponse createBillLike(User user, Bill bill) {
        var billLike = BillLike.builder()
                .bill(bill)
                .user(user)
                .build();
        billLikeRepository.save(billLike);
        return BillLikeResponse.from(true);
    }

    private BillLikeResponse deleteBillLike(BillLike billLike) {
        billLikeRepository.deleteById(billLike.getId());
        return BillLikeResponse.from(false);
    }

    private void isEqual(boolean dbValue, boolean parameterValue) {
        if (dbValue == parameterValue) {
            throw new LikeException.UpdateParameterException(Map.of(UPDATE_PARAMETER ,Boolean.toString(parameterValue)));
        }
    }



}
