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

    public CongressmanDto getCongressman(String congressmanId) {
        Congressman congressman = congressmanRepository.findByIdWithParty(congressmanId)
                .orElseThrow(() -> new EntityNotFoundException("해당 의원이 존재하지 않습니다."));
        return CongressmanDto.fromCongressman(congressman);
    }

}

