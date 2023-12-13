package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.BaseResponse;
import com.everyones.lawmaking.common.dto.CongressmanDto;
import com.everyones.lawmaking.common.dto.response.MainFeedBillResponse;
import com.everyones.lawmaking.service.BillService;
import com.everyones.lawmaking.service.CongressmanService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/bill")
public class BillController {
    private final BillService billService;
    private final CongressmanService congressmanService;
    @GetMapping("/mainfeed")

    public ResponseEntity<Map<String, Object>> getNext3Bills(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "size", required = true) int size,
            Pageable pageable) {
        try {
            var result = billService.getNext3Bills(page, pageable);
            var resp = BaseResponse.generateSuccessResponse(result);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            var resp = BaseResponse.generateErrorResponse(false, 500, e.getMessage(), null);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/mainfeed/stage")
    public ResponseEntity<Map<String, Object>> getNext3BillsWithStage(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "size", required = true) int size,
            @RequestParam(name = "stage", required = true) String stage,
            Pageable pageable) {
        try {
            var result = billService.getNext3BillsWithStage(page, pageable, stage);
            var resp = BaseResponse.generateSuccessResponse(result);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            var resp = BaseResponse.generateErrorResponse(false, 500, e.getMessage(), null);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail/{bill_id}")
    public ResponseEntity<Map<String, Object>> getBillWtihDeatail(
            @PathVariable("bill_id") String billId) {
        try {
            var result = billService.getBillWtihDeatail(billId);
            var resp = BaseResponse.generateSuccessResponse(result);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            var resp = BaseResponse.generateErrorResponse(false, 500, e.getMessage(), null);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/mainfeed/{id}")
//    public ResponseEntity<MainFeedBillResponse> getCongressman(
//            @PathVariable String id,
//            @RequestParam("page") int page,
//            @RequestParam("size") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        MainFeedBillResponse response = congressmanService.getCongressmanDetails(id, pageable);
//        return ResponseEntity.ok(response);
//    }
//    @GetMapping("/mainfeed/{id}")
//    public ResponseEntity<MainFeedBillResponse> getCongressman(
//            @PathVariable String id,
//            @RequestParam("page") int page,
//            @RequestParam("size") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        MainFeedBillResponse congressmanDto = congressmanService.getCongressmanDetails(id, pageable);
//        return ResponseEntity.ok(congressmanDto);
//    }
//    @GetMapping("/mainfeed/{id}")
//    public ResponseEntity<CongressmanDto> getCongressman(@PathVariable String id) {
//        CongressmanDto congressmanDto = congressmanService.getCongressmanDetails(id);
//        return ResponseEntity.ok(congressmanDto);
//    }


}
