package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.domain.entity.BillLike;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillLikeResponse {

    @NotNull
    private boolean likeChecked;

    public static BillLikeResponse from(boolean likeChecked) {
        return BillLikeResponse.builder()
                .likeChecked(likeChecked)
                .build();
    }

}
