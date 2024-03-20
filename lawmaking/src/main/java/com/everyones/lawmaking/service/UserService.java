package com.everyones.lawmaking.service;

import com.everyones.lawmaking.domain.entity.User;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserId(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResponseCode.INTERNAL_SERVER_ERROR));
    }

}
