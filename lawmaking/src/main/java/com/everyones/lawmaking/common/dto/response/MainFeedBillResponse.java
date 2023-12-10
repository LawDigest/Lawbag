package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.BillDto;
import lombok.*;

import java.util.List;


@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class MainFeedBillResponse {
    private List<BillDto> Bills;

    public static MainFeedBillResponse of(List<BillDto> billList) {
        return MainFeedBillResponse.builder()
                .Bills(billList)
                .build();
    }
}
