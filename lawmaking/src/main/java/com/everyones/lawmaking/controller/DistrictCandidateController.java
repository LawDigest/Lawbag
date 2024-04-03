package com.everyones.lawmaking.controller;


import com.everyones.lawmaking.common.dto.response.DistrictCandidateListResponse;
import com.everyones.lawmaking.common.dto.response.ProportionalCandidateListResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.everyones.lawmaking.global.SwaggerConstants.EXAMPLE_ERROR_500_CONTENT;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/districtCandidate")
@Tag(name="지역구 후보자 관련 조회 API", description = "지역구 후보자 관련 데이터를 가져오는 API입니다.")
public class DistrictCandidateController {

    private final Facade facade;

    @Operation(summary = "지역구 후보자 조회 ", description = "지역구 후보자 조회하기")
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
    @GetMapping("/simple_list")
    public BaseResponse<DistrictCandidateListResponse> followParty(
            Authentication authentication,
            @Parameter(example = "1", description = "지역구 Id")
            @RequestParam("district_id") long districtId,
            @Parameter(example = "0", description = "스크롤할 때마다 page값을 0에서 1씩 늘려주면 됩니다.")
            @RequestParam(name = "page") int page){
        var pageable = PageRequest.of(page, 5);

        var result = facade.getDistrictCandidateList(districtId, pageable);
        return BaseResponse.ok(result);
    }

}
