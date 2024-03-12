package com.everyones.lawmaking.controller;


import com.everyones.lawmaking.common.dto.CongressmanDto;
import com.everyones.lawmaking.common.dto.response.BillListResponse;
import com.everyones.lawmaking.facade.Facade;
import com.everyones.lawmaking.global.BaseResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

        var CongressmanDto = facade.getCongressman(congressmanId);

        return BaseResponse.ok(CongressmanDto);

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
            @RequestParam("is_represent") Boolean isRepresent,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (isRepresent == true) {
            var representativeBills = facade.getBillsFromRepresentativeProposer(congressmanId, pageable);
            return BaseResponse.ok(representativeBills);
        }
        var publicProposerBills = facade.getBillsFromPublicProposer(congressmanId, pageable);
        return BaseResponse.ok(publicProposerBills);

    }
}
