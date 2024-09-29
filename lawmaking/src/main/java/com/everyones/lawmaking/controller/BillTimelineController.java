package com.everyones.lawmaking.controller;


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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static com.everyones.lawmaking.global.SwaggerConstants.EXAMPLE_ERROR_500_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/time-line")
@Tag(name = "법안 타임라인 API", description = "법안 타임라인 API")
public class BillTimelineController {
    private final Facade facade;

    @Operation(summary = "타임라인 피드 조회", description = "타임라인에 들어갈 데이터를 가져옵니다.")
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
    @GetMapping("/feed")
    public BaseResponse<?> getTimeline(
            @Parameter(example = "2024-08-20", description = "날짜별 법안 조회")
            @RequestParam(value = "billProposeDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate billProposeDate
    ) {
        if (billProposeDate == null) {
            billProposeDate = LocalDate.now();
        }
        var result = facade.getTimeline(billProposeDate);
        return BaseResponse.ok(result);
    }

//    @GetMapping("/bill-state")
//    public BaseResponse<?> getBillStateCount() {
//        var result = facade.getBillStateCount();
//        return BaseResponse.ok(result);
//    }
}
