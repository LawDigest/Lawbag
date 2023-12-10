package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.common.dto.response.GetBillWithMainFeedRes;
import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mainfeed")
public class BillController {
    private final BillService billService;

    @GetMapping
    public List<BillDto> getNext3Bills(@RequestParam(required = false) String lastBillId,
                                       @RequestParam(required = false) LocalDateTime lastProposeDate) {
        return billService.getNext3Bills(lastBillId, lastProposeDate);
    }
}
