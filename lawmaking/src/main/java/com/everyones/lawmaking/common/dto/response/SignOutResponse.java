package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.AuthInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignOutResponse {

    @NotNull
    private String socialId;

    @NotNull
    private String provider;

    public static SignOutResponse of(AuthInfo authInfo) {
        return SignOutResponse.builder()
                .socialId(authInfo.getSocialId())
                .provider(authInfo.getProvider().name())
                .build();
    }

}
