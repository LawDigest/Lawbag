package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.BillDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SearchBillResponse {
    private List<BillDto> searchResponse;

    private PaginationResponse paginationResponse;

    public SearchBillResponse of(List<BillDto> searchBillResponse, PaginationResponse paginationResponse) {
        return SearchBillResponse.builder()
                .searchResponse(searchBillResponse)
                .paginationResponse(paginationResponse)
                .build();
    }
}
