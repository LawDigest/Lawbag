package com.everyones.lawmaking.common.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.data.domain.Slice;

@Getter
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaginationResponse {
    private boolean isLastPage;
    private int pageNumber;


    public static <T> PaginationResponse fromSlice(Slice<T> sliceObject) {
        return PaginationResponse.<T>builder()
                .isLastPage(!sliceObject.hasNext())
                .pageNumber(sliceObject.getNumber())
                .build();
    }
}