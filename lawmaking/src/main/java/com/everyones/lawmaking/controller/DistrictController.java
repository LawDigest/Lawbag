package com.everyones.lawmaking.controller;


import com.everyones.lawmaking.common.dto.response.DistrictIdResponse;
import com.everyones.lawmaking.common.dto.response.DistrictResponse;
import com.everyones.lawmaking.common.dto.response.PartyDetailResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.everyones.lawmaking.global.SwaggerConstants.EXAMPLE_ERROR_500_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/district")
@Tag(name = "선거구 관련 API", description = "선거구 관련 데이터를 가져오는 API입니다.")
public class DistrictController {
    private final Facade facade;

    @Operation(summary = "도시, 지역구별 선거구 조회", description = "도시, 지역구별 선거구 데이터 조회")
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
    @GetMapping("/list")
    public BaseResponse<List<DistrictResponse>> getDistrictList(@Parameter(example = "서울특별시", description = "도시 이름")
                                                       @RequestParam(value = "city_name", required = false) String cityName,
                                                                @Parameter(example = "종로구", description = "지역구 이름")
                                                                @RequestParam(value="gu_name",required = false)        String guName
                                                             ) {

        var response = facade.getDistrictList(cityName,guName);
        return BaseResponse.ok(response);
    }

    @Operation(summary = "선거구Id 조회", description = "선거구Id 조회 조회")
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
    @GetMapping("")
    public BaseResponse<DistrictIdResponse> getDistrictId(@Parameter(example = "서울특별시", description = "도시 이름")
                                                                @RequestParam(value = "city_name") String cityName,
                                                          @Parameter(example = "종로구", description = "지역구 이름")
                                                                @RequestParam(value="gu_name") String guName,
                                                          @Parameter(example = "종로구", description = "지역구 이름")
                                                                  @RequestParam(value="district_name") String districtName) {

        var response = facade.getDistrictId(cityName,guName,districtName);
        return BaseResponse.ok(response);
    }

}
