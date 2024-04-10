package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.response.FollowingPartyResponse;
import com.everyones.lawmaking.common.dto.response.LikingCongressmanResponse;
import com.everyones.lawmaking.common.dto.response.UserMyPageInfoResponse;
import com.everyones.lawmaking.facade.Facade;
import com.everyones.lawmaking.global.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.everyones.lawmaking.global.SwaggerConstants.EXAMPLE_ERROR_500_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
@Tag(name = "유저 관련 API", description = "유저 관련 API 호출")
public class UserController {
    private final Facade facade;

    @Operation(summary = "유저 정보 조회", description = "마이 페이지에서 유저의 기본 정보 조회 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 (문제 지속시 BE팀 문의)",
                    content = {@Content(
                            mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(value = EXAMPLE_ERROR_500_CONTENT)
                    )}
            ),
    })
    @GetMapping("/info")
    public BaseResponse<UserMyPageInfoResponse> getUserMyPageInfo(
            Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var userId = Long.parseLong(userDetails.getUsername());
        var result = facade.getUserMyPageInfo(userId);
        return BaseResponse.ok(result);
    }

    @Operation(summary = "팔로우한 정당 조회", description = "마이 페이지에서 유저가 팔로우한 정당을 조회합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 (문제 지속시 BE팀 문의)",
                    content = {@Content(
                            mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(value = EXAMPLE_ERROR_500_CONTENT)
                    )}
            ),
    })
    @GetMapping("/following/party")
    public BaseResponse<List<FollowingPartyResponse>> getFollowingParty (
            Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var userId = Long.parseLong(userDetails.getUsername());
        var result = facade.getFollowingParty(userId);
        return BaseResponse.ok(result);

    }

    @Operation(summary = "팔로우한 의원 조회", description = "마이 페이지에서 유저가 팔로우한 의원을 조회합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 (문제 지속시 BE팀 문의)",
                    content = {@Content(
                            mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(value = EXAMPLE_ERROR_500_CONTENT)
                    )}
            ),
    })
    @GetMapping("/liking/congressman")
    public BaseResponse<List<LikingCongressmanResponse>> getLikingCongressman (
            Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var userId = Long.parseLong(userDetails.getUsername());
        var result = facade.getLikingCongressman(userId);
        return BaseResponse.ok(result);

    }


}
