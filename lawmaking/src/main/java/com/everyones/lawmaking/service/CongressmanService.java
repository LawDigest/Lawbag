package com.everyones.lawmaking.service;


import com.everyones.lawmaking.common.dto.response.CongressmanResponse;
import com.everyones.lawmaking.common.dto.response.LikingCongressmanResponse;
import com.everyones.lawmaking.domain.entity.Congressman;
import com.everyones.lawmaking.repository.CongressmanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CongressmanService {
    private final CongressmanRepository congressmanRepository;

    public Congressman findCongressman(String congressmanId) {
        return congressmanRepository.findByIdWithParty(congressmanId)
                .orElseThrow(() -> new EntityNotFoundException("해당 의원이 존재하지 않습니다."));
    }

    public CongressmanResponse getCongressman(String congressmanId) {
        var congressman = findCongressman(congressmanId);
        return CongressmanResponse.fromCongressman(congressman);
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

}

