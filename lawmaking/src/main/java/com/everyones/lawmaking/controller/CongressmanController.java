package com.everyones.lawmaking.controller;


import com.everyones.lawmaking.global.BaseResponse;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.service.CongressmanService;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import static com.everyones.lawmaking.global.SwaggerConstants.EXAMPLE_ERROR_500_CONTENT;
import java.util.Map;

//의원 자세히 보기 api

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/congressman")
@Tag(name="의원 관련 조회 API", description = "의원 관련 데이터를 가져오는 API입니다.")
public class CongressmanController {
    private final CongressmanService congressmanService;


    @Operation(summary = "의원 상세 조회", description = "의원 상세페이지에 들어갈 데이터를 가져옵니다. 추후 API 분리예정")
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
    public BaseResponse<Map<String, Object>> getCongressmanBillsDetails(
            @Parameter(example = "04T3751T", description = "의원 Id")
            @RequestParam("congressman_id")
            String congressmanId,
            @Parameter(example = "대표", description = "공동대표발의안 또는 대표발의 의안 조회 여부")
            @Schema(type = "string", allowableValues = {"대표", "공통"})
            @RequestParam(value = "stage", required = true)
            String stage,
            @Parameter(example = "0", description = "스크롤할 때마다 page값을 0에서 1씩 늘려주면 됩니다.")
            @RequestParam(name = "page", required = true)
            int page,
            @Parameter(example = "3", description = "한번에 가져올 데이터 크기를 의미합니다.")
            @RequestParam(name = "size", required = true)
            int size) {

        Pageable pageable = PageRequest.of(page, size);
        Map<String, Object> response;

        if ("대표".equals(stage)) {
            response = congressmanService.getCongressmanDetails(congressmanId, pageable);
        } else if ("공동".equals(stage)) {
            response = congressmanService.getCongressmanCoSponsorshipDetails(congressmanId, pageable);
        } else {
            // 유효하지 않은 stage 값 처리
            throw new CustomException(ResponseCode.INVALID_QUERY_PARAMETER);
        }

        return BaseResponse.ok(response);
    }
}
