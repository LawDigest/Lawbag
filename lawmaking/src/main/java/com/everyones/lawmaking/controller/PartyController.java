package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.BaseResponse;
import com.everyones.lawmaking.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/party")
public class PartyController {
    private final PartyService partyService;

    @GetMapping("/detail/{party_id}")
    public ResponseEntity<?> getPartyDetail(@PathVariable("party_id") long partyId,
                                            @RequestParam("stage") String stage, // "represent" or "public"
                                            @RequestParam("page") int page,
                                            @RequestParam("size") int size,
    Pageable pageable) {
        {
            try {
                var result = partyService.getPartyDetail(page, pageable, partyId, stage);
                var resp = BaseResponse.generateSuccessResponse(result);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } catch (Exception e) {
                var resp = BaseResponse.generateErrorResponse(false, 500, e.getMessage(), null);
                return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }




}
