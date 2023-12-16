package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.BaseResponse;
import com.everyones.lawmaking.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/party")
public class PartyController {
    private final PartyService partyService;

    @GetMapping("/detail/{partyId}")
    public ResponseEntity<?> getPartyDetail(@PathVariable("partyId") long partyId) {
        {
            try {
                var result = partyService.getPartyDetail(partyId);
                var resp = BaseResponse.generateSuccessResponse(result);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } catch (Exception e) {
                var resp = BaseResponse.generateErrorResponse(false, 500, e.getMessage(), null);
                return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }




}
