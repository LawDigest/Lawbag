package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.UserMyPageInfoResponse;
import com.everyones.lawmaking.domain.entity.User;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResponseCode.INTERNAL_SERVER_ERROR));
    }

    public UserMyPageInfoResponse getUserMyPageInfo(long userId) {
        var user = findById(userId);
        return UserMyPageInfoResponse.from(user);
    }

    public List<User> getUserByLikedBillId(String billId) {
        return userRepository.findAllByBillId(billId);
    }

    public List<User> getUserByLikedCongressmanId(String congressmanId) {
        return userRepository.findAllByCongressmanId(congressmanId);
    }

}
