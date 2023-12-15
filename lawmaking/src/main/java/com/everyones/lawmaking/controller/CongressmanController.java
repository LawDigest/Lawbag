package com.everyones.lawmaking.controller;


import com.everyones.lawmaking.common.dto.response.CongressmanDetailResponse;

import com.everyones.lawmaking.service.CongressmanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

//의원 자세히 보기 api
//특정 의원이 대표 발의 법안 api
//특정 의원이 공동 발의 법안 api
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/congressman")
@Tag(name="Congressman Controller", description = "의원 상세 페이지")
public class CongressmanController {
    private final CongressmanService congressmanService;
    @GetMapping("/detail/{congressman_id}")
    public ResponseEntity<CongressmanDetailResponse> getCongressmanDetails(
            @PathVariable("congressman_id") String congressmanId,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        CongressmanDetailResponse response = congressmanService.getCongressmanDetails(congressmanId, pageable);
        return ResponseEntity.ok(response);
    }
}
