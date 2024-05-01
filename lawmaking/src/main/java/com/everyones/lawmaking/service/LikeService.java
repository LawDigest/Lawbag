package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.BillLikeResponse;
import com.everyones.lawmaking.common.dto.response.CongressmanLikeResponse;
import com.everyones.lawmaking.common.dto.response.PartyFollowResponse;
import com.everyones.lawmaking.domain.entity.*;
import com.everyones.lawmaking.global.error.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.global.error.ErrorCode;
import com.everyones.lawmaking.repository.BillLikeRepository;
import com.everyones.lawmaking.repository.CongressmanLikeRepository;
import com.everyones.lawmaking.repository.PartyFollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LikeService {
    private final BillLikeRepository billLikeRepository;
    private final CongressmanLikeRepository congressmanLikeRepository;
    private final PartyFollowRepository partyFollowRepository;

    public Boolean getCongressmanLike(String congressmanId, long userId) {
        var congressmanLike = congressmanLikeRepository.findByUserIdAndCongressmanId(userId, congressmanId);
        return congressmanLike.map(CongressManLike::isLikeChecked).orElse(false);
    }
    public Boolean getFollowParty(long partyId, long userId) {
        var partyFollow = partyFollowRepository.findByUserIdAndPartyId(partyId, userId);
        return partyFollow.map(PartyFollow::isFollowChecked).orElse(false);
    }

    public Boolean getBillLikeChecked(String billId, long userId) {
        var billLike = billLikeRepository.findByUserIdAndBillId(userId, billId);
        return billLike.map(BillLike::isLikeChecked).orElse(false);
    }

    public BillLikeResponse likeBill(User user, Bill bill, boolean likeChecked) {
        var billLike = billLikeRepository.findByUserIdAndBillId(user.getId(), bill.getId());

        return billLike.isPresent() ? updateBillLike(billLike.get(), likeChecked) : createBillLike(user, bill, likeChecked);
    }


    public CongressmanLikeResponse likeCongressman(User user, Congressman congressman, boolean likeChecked) {
        var congressmanLike = congressmanLikeRepository.findByUserIdAndCongressmanId(user.getId(), congressman.getId());

        return congressmanLike.isPresent() ? updateCongressmanLike(congressmanLike.get(), likeChecked) : createCongressmanLike(user ,congressman, likeChecked);
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


    private CongressmanLikeResponse createCongressmanLike(User user, Congressman congressman, boolean likeChecked) {
        isEqual(false, likeChecked);
        var congressmanLike = CongressManLike.builder()
                .congressman(congressman)
                .user(user)
                .likeChecked(likeChecked)
                .build();
        congressmanLikeRepository.save(congressmanLike);

        return CongressmanLikeResponse.from(congressmanLike);
    }

    private CongressmanLikeResponse updateCongressmanLike(CongressManLike congressmanLike, boolean likeChecked) {
        isEqual(congressmanLike.isLikeChecked(), likeChecked);
        var updatedCongressmanLike = CongressManLike.builder()
                .id(congressmanLike.getId())
                .congressman(congressmanLike.getCongressman())
                .user(congressmanLike.getUser())
                .likeChecked(likeChecked)
                .build();
        congressmanLikeRepository.save(updatedCongressmanLike);
        return CongressmanLikeResponse.from(updatedCongressmanLike);
    }

    private BillLikeResponse createBillLike(User user, Bill bill, boolean likeChecked) {
        isEqual(false, likeChecked);
        var billLike = BillLike.builder()
                .bill(bill)
                .user(user)
                .likeChecked(likeChecked)
                .build();
        billLikeRepository.save(billLike);

        return BillLikeResponse.from(billLike);
    }


    private BillLikeResponse updateBillLike(BillLike billLike, boolean likeChecked) {
        isEqual(billLike.isLikeChecked(), likeChecked);
        var updatedBillLike = BillLike.builder()
                .id(billLike.getId())
                .bill(billLike.getBill())
                .user(billLike.getUser())
                .likeChecked(likeChecked)
                .build();
        billLikeRepository.save(updatedBillLike);
        return BillLikeResponse.from(updatedBillLike);
    }

    private void isEqual(boolean dbValue, boolean parameterValue) {
        if (dbValue == parameterValue) {
            throw new CustomException(ErrorCode.BAD_UPDATE_PARAMETER);
        }
    }



}
