package com.everyones.lawmaking.controller;


import com.everyones.lawmaking.global.BaseResponse;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.service.CongressmanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Map;

//의원 자세히 보기 api

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/congressman")
@Tag(name="Congressman Controller", description = "의원 상세 페이지")
public class CongressmanController {
    private final CongressmanService congressmanService;

    @GetMapping("/detail")
    public BaseResponse<Map<String, Object>> getCongressmanBillsDetails(
            @RequestParam("congressman_id") String congressmanId,
            @RequestParam("stage") String stage, // "represent" or "public"
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Map<String, Object> response;

        if ("대표".equals(stage)) {
            response = congressmanService.getCongressmanDetails(congressmanId, pageable);
        } else if ("공동".equals(stage)) {
            response = congressmanService.getCongressmanCoSponsorshipDetails(congressmanId, pageable);
        } else {
            // 유효하지 않은 stage 값 처리
            throw new CustomException(ResponseCode.INVALID_QUERY_PARAMETER);
        }

        return BaseResponse.ok(response);
    }
}
