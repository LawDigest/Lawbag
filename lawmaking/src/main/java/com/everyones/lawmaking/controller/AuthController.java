package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.response.WithdrawResponse;
import com.everyones.lawmaking.facade.Facade;
import com.everyones.lawmaking.global.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.everyones.lawmaking.global.SwaggerConstants.EXAMPLE_ERROR_500_CONTENT;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
@Tag(name = "인증 API", description = "인증 관련 API")
public class AuthController {

    private final Facade facade;

    @Operation(summary = "회원탈퇴", description = "회원탈퇴 합니다.")
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
    @DeleteMapping("/user/withdraw")
    public BaseResponse<WithdrawResponse> withdraw(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        var userDetails = (UserDetails) authentication.getPrincipal();

        var result = facade.withdraw(userDetails.getUsername(),request,response);
        return BaseResponse.ok(result);
    }



    @Operation(summary = "토큰 재발급", description = "토큰 재발급 api를 호출합니다.")
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
    @PostMapping("/reissue/token")
    public BaseResponse<String> reissueToken(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        facade.reissueToken(request,response);
        return BaseResponse.ok("success");
    }
}
