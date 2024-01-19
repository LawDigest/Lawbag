package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.response.BillDetailDto;
import com.everyones.lawmaking.global.BaseResponse;
import com.everyones.lawmaking.common.dto.response.MainFeedBillResponse;
import com.everyones.lawmaking.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/bill")
public class BillController {
    private final BillService billService;

    @GetMapping("/mainfeed")

    public BaseResponse<MainFeedBillResponse> getNext3Bills(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "size", required = true) int size,
            Pageable pageable) {

        var result = billService.getNext3Bills(page, pageable);
        return BaseResponse.ok(result);

    }
    @GetMapping("/mainfeed/stage")
    public BaseResponse<MainFeedBillResponse> getNext3BillsWithStage(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "size", required = true) int size,
            @RequestParam(name = "stage", required = true) String stage,
            Pageable pageable) {

        var result = billService.getNext3BillsWithStage(page, pageable, stage);
        return BaseResponse.ok(result);

    }

    @GetMapping("/detail/{bill_id}")
    public BaseResponse<BillDetailDto> getBillWtihDeatail(
            @PathVariable("bill_id") String billId) {
        var result = billService.getBillWtihDeatail(billId);
        return BaseResponse.ok(result);

    }

    @GetMapping("/search/summary")
    public BaseResponse<MainFeedBillResponse> searchBills(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "size", required = true) int size,
            @RequestParam(name = "summaryquestion", required = true) String summaryquestion,
            Pageable pageable) {

        var result = billService.searchBillsBySummary(page, pageable, summaryquestion);
        return BaseResponse.ok(result);

    }


}
