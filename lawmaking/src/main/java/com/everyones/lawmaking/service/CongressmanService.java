package com.everyones.lawmaking.service;


import com.everyones.lawmaking.common.dto.CongressmanDto;
import com.everyones.lawmaking.domain.entity.Congressman;
import com.everyones.lawmaking.repository.CongressmanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    public CongressmanDto getCongressman(String congressmanId) {
        var congressman = findCongressman(congressmanId);
        return CongressmanDto.fromCongressman(congressman);
    }

    @Transactional
    public void updateCongressmanLikeCount(Congressman congressman, boolean likeChecked) {
        var likeCount = likeChecked ? congressman.getLikeCount() + 1 : congressman.getLikeCount() - 1;
        congressman.setLikeCount(likeCount);
        congressmanRepository.save(congressman);
    }

}

