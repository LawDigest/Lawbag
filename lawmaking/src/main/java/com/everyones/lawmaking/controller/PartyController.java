package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.response.PartyDetailResponse;
import com.everyones.lawmaking.global.BaseResponse;
import com.everyones.lawmaking.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/party")
public class PartyController {
    private final PartyService partyService;

    @GetMapping("/detail/{party_id}")
    public BaseResponse<PartyDetailResponse> getPartyDetail(@PathVariable("party_id") long partyId,
                                                            @RequestParam("stage") String stage, // "represent" or "public"
                                                            @RequestParam("page") int page,
                                                            @RequestParam("size") int size,
                                                            Pageable pageable) {

        var result = partyService.getPartyDetail(page, pageable, partyId, stage);
        return BaseResponse.ok(result);

    }

}
