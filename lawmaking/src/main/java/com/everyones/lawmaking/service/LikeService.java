package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.BillLikeResponse;
import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.domain.entity.BillLike;
import com.everyones.lawmaking.domain.entity.User;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.repository.BillLikeRepository;
import com.everyones.lawmaking.repository.CongressmanLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LikeService {
    private final BillLikeRepository billLikeRepository;
    private final CongressmanLikeRepository congressmanLikeRepository;

    // TODO: 반환형 점검
    public BillLikeResponse likeBill(User user, Bill bill) {
        var billLike = billLikeRepository.findByIds(user.getId(), bill.getId());
        var modifiedBillLike = billLike.isPresent() ? updateBillLike(billLike.get()) : createBillLike(user, bill);

        return modifiedBillLike;
    }


    private BillLikeResponse createBillLike(User user, Bill bill) {
        var billLike = BillLike.builder()
                .bill(bill)
                .user(user)
                .likeChecked(true)
                .build();
        billLikeRepository.save(billLike);

        return BillLikeResponse.from(billLike);
    }


    private BillLikeResponse updateBillLike(BillLike billLike) {
        var updatedBillLike = BillLike.builder()
                .id(billLike.getId())
                .bill(billLike.getBill())
                .user(billLike.getUser())
                .likeChecked(!billLike.isLikeChecked())
                .build();
        billLikeRepository.save(updatedBillLike);
        return BillLikeResponse.from(updatedBillLike);
    }
}
