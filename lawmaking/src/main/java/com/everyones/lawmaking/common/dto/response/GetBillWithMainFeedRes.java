package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.BillDto;
import lombok.*;

import java.util.List;


@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class GetBillWithMainFeedRes {
    private int page;
    private List<BillDto> Bills;

//    public static GetBillWithMainFeedRes of(BillDto billDto) {
//
//    }
}
