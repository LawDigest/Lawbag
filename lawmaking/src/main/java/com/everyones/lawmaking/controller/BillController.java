package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.BaseResponse;
import com.everyones.lawmaking.common.dto.BillSearchDto;
import com.everyones.lawmaking.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/bill")
public class BillController {
    private final BillService billService;

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

    @GetMapping("/search/summary")
    public ResponseEntity<Map<String, Object>> searchBills(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "size", required = true) int size,
            @RequestParam(name = "summaryquestion", required = true) String summaryquestion,
            Pageable pageable) {
        try{
            var result = billService.searchBillsBySummary(page, pageable, summaryquestion);
            var resp = BaseResponse.generateSuccessResponse(result);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            var resp = BaseResponse.generateErrorResponse(false, 500, e.getMessage(), null);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
//    public ResponseEntity<Map<String, Object>> getNext3BillsWithStage(
//            @RequestParam(name = "page", required = true) int page,
//            @RequestParam(name = "size", required = true) int size,
//            @RequestParam(name = "stage", required = true) String stage,
//            Pageable pageable) {
//        try {
//            var result = billService.getNext3BillsWithStage(page, pageable, stage);
//            var resp = BaseResponse.generateSuccessResponse(result);
//            return new ResponseEntity<>(resp, HttpStatus.OK);
//        } catch (Exception e) {
//            var resp = BaseResponse.generateErrorResponse(false, 500, e.getMessage(), null);
//            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
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
