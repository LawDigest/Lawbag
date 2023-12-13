    package com.everyones.lawmaking.common.dto.response;

    import com.everyones.lawmaking.common.dto.BillDto;
    import com.everyones.lawmaking.common.dto.CongressmanDto;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import com.fasterxml.jackson.databind.PropertyNamingStrategy;
    import com.fasterxml.jackson.databind.annotation.JsonNaming;
    import lombok.*;
    import org.springframework.data.domain.Slice;

    import java.util.List;


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    public class MainFeedBillResponse {
        private PaginationResponse paginationResponse;

        private List<BillDto> Bills;

        private CongressmanDto congressman;

        // PaginationResponse와 CongressmanDto를 사용하는 경우
        public static MainFeedBillResponse of(PaginationResponse paginationResponse, CongressmanDto congressman) {
            MainFeedBillResponse response = new MainFeedBillResponse();
            response.paginationResponse = paginationResponse;
            response.congressman = congressman;
            return response;
        }
        // PaginationResponse와 List<BillDto>를 사용하는 경우
//        public static MainFeedBillResponse of(PaginationResponse paginationResponse, List<BillDto> bills) {
//            MainFeedBillResponse response = new MainFeedBillResponse();
//            response.paginationResponse = paginationResponse;
//            response.bills = bills;
//            return response;
//        }

        public static MainFeedBillResponse of(PaginationResponse paginationResponse,List<BillDto> billList) {
            return MainFeedBillResponse.builder()
                    .Bills(billList)
                    .paginationResponse(paginationResponse)
                    .build();
        }
    }
