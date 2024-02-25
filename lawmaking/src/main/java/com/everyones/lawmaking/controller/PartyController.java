package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.response.PartyDetailResponse;
import com.everyones.lawmaking.global.BaseResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/v1/party")
//@Tag(name = "정당 관련 API", description = "정당 관련 API 호출")
//public class PartyController {
//    private final PartyService partyService;
//
//    @GetMapping("/detail/{party_id}")
//    public BaseResponse<PartyDetailResponse> getPartyDetail(@Parameter(example = "1", description = "정당 id")
//                   @PathVariable("party_id") long partyId,
//                   @Parameter(example = "대표", description = "공동대표발의안 또는 대표발의 의안 조회 여부")
//                   @Schema(type = "string", allowableValues = {"대표", "공통"})
//                   @RequestParam("stage") String stage,
//                   @Parameter(example = "0", description = "스크롤할 때마다 page값을 0에서 1씩 늘려주면 됩니다.")
//                   @RequestParam(name = "page", required = true) int page,
//                   @Parameter(example = "3", description = "한번에 가져올 데이터 크기를 의미합니다.")
//                   @RequestParam(name = "size", required = true) int size) {
//        var pageable = PageRequest.of(page, size);
//        var result = partyService.getPartyDetail(page, pageable, partyId, stage);
//        return BaseResponse.ok(result);
//
//    }
//
//}
