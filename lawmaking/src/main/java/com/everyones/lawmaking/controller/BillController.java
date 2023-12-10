package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.BaseResponse;
import com.everyones.lawmaking.common.dto.response.MainFeedBillResponse;
import com.everyones.lawmaking.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Collections;

@RequiredArgsConstructor
@Controller
@RequestMapping("/v1/bill")
public class BillController {
    private final BillService billService;

    @GetMapping("/mainfeed")
    public ResponseEntity<?> getNext3Bills(@RequestParam(required = false) String lastBillId,
                                                              @RequestParam(required = false) LocalDateTime lastProposeDate) {
        try {
            var result = billService.getNext3Bills(lastBillId, lastProposeDate);
            var resp = BaseResponse.generateSuccessResponse(Collections.singletonList(result));
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            var resp = BaseResponse.generateErrorResponse(false, 500, e.getMessage(), null);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
