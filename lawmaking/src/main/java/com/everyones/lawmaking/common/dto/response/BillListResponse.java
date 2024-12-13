package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.bill.BillDto;
import com.everyones.lawmaking.domain.entity.Bill;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import org.springframework.data.domain.Slice;

@Builder
@Data
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillListResponse {
    private PaginationResponse paginationResponse;

    private List<BillDto> billList;

    public static BillListResponse of(PaginationResponse paginationResponse, List<BillDto> billList) {
        return BillListResponse.builder()
                .paginationResponse(paginationResponse)
                .billList(billList)
                .build();
    }
    public static BillListResponse from(Slice<Bill> billSlice) {
        return BillListResponse.builder()
                .paginationResponse(PaginationResponse.from(billSlice))
                .billList(toBillDtoList(billSlice.getContent()))
                .build();
    }
    private static List<BillDto> toBillDtoList(List<Bill> billList) {
        return billList.stream()
                .map(BillDto::from)
                .toList();
    }

}
