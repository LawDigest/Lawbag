package com.everyones.lawmaking.controller;


import com.everyones.lawmaking.common.dto.CongressmanDto;
import com.everyones.lawmaking.common.dto.response.BillListResponse;
import com.everyones.lawmaking.common.dto.response.CongressmanLikeResponse;
import com.everyones.lawmaking.facade.Facade;
import com.everyones.lawmaking.global.BaseResponse;
import com.everyones.lawmaking.global.auth.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.everyones.lawmaking.global.SwaggerConstants.EXAMPLE_ERROR_500_CONTENT;


@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/congressman")
@Tag(name="의원 관련 조회 API", description = "의원 관련 데이터를 가져오는 API입니다.")
public class CongressmanController {
    private final Facade facade;

    @Operation(summary = "의원 상세 조회", description = "의원 상세페이지에 들어갈 데이터를 가져옵니다.")
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
    @GetMapping("/detail")
    public BaseResponse<CongressmanDto> getCongressmanDetails(
            @Parameter(example = "04T3751T", description = "의원 Id")
            @RequestParam("congressman_id")
            String congressmanId) {

        var congressmanDto = facade.getCongressman(congressmanId);

        return BaseResponse.ok(congressmanDto);

    }


    @Operation(summary = "의원의 발의안 리스트 조회", description = "의원이 발의한 법안 데이터 조회")
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
    @GetMapping("/bill_info")
    public BaseResponse<BillListResponse> getCongressmanBills(
            @Parameter(example = "04T3751T", description = "의원 Id")
            @RequestParam("congressman_id") String congressmanId,
            @Parameter(example = "true", description = "해당 의원의 법안 대표 발의 기준, 공동 발의 기준 여부")
            @Schema(type = "boolean", allowableValues = {"true", "false"})
            @RequestParam("type") String type,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (type.equals("true")) {
            var representativeBills = facade.getBillsFromRepresentativeProposer(congressmanId, pageable);
            return BaseResponse.ok(representativeBills);
        }
        var publicProposerBills = facade.getBillsFromPublicProposer(congressmanId, pageable);
        return BaseResponse.ok(publicProposerBills);
    }

    @Operation(summary = "의원 좋아요", description = "의원이 좋아요 클릭")
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
    @PatchMapping("/like")
    public BaseResponse<CongressmanLikeResponse> likeCongressman(
            Authentication authentication,
            @Parameter(example = "04T3751T", description = "의원 Id")
            @RequestParam("congressman_id") String congressmanId,
            @Schema(type = "boolean", allowableValues = {"true", "false"})
            @RequestParam("like_checked") boolean likeChecked
    ) {
        var user = (PrincipalDetails) authentication.getPrincipal();
        var userId = user.getUserId();
        var result = facade.likeCongressman(userId, congressmanId, likeChecked);
        return BaseResponse.ok(result);
    }
}
