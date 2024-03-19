package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.BillLike;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BillLikeResponse {

    @NotNull
    private long userId;

    @NotNull
    private String billId;

    @NotNull
    private boolean likeCheckd;


    public static BillLikeResponse from(BillLike billLike) {
        return BillLikeResponse.builder()
                .billId(billLike.getBill().getId())
                .userId(billLike.getUser().getId())
                .likeCheckd(billLike.isLikeChecked())
                .build();

    }
}
