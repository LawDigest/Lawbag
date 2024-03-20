package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.CongressManLike;
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
public class CongressmanLikeResponse {

    @NotNull
    private String congressmanId;

    @NotNull
    private boolean likeChecked;

    public static CongressmanLikeResponse from(CongressManLike congressmanLike){
        return CongressmanLikeResponse.builder()
                .congressmanId(congressmanLike.getCongressMan().getId())
                .likeChecked(congressmanLike.isLikeChecked())
                .build();
    }

}
