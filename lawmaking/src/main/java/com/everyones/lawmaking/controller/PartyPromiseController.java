package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.response.PartyPromiseResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.everyones.lawmaking.global.SwaggerConstants.EXAMPLE_ERROR_500_CONTENT;
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/proportional_candidate")
@Tag(name = "정당 공약 관련 API", description = "정당 공약 관련 API 호출")
public class PartyPromiseController {

    private final Facade facade;


    @Operation(summary = "정당 공약 리스트 조회", description = "정당 공약 데이터 조회")
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
    @GetMapping("/public/promise")
    public BaseResponse<PartyPromiseResponse> getPartyPromise(@Parameter(example = "1", description = "정당 id")
                                                              @RequestParam("party_id") long partyId,
                                                              @Parameter(example = "0", description = "스크롤할 때마다 page값을 0에서 1씩 늘려주면 됩니다.")
                                                              @RequestParam(name = "page") int page) {
        var pageable = PageRequest.of(page, 5);

        var response = facade.getPartyPromise(partyId,pageable);
        return BaseResponse.ok(response);
    }
}
