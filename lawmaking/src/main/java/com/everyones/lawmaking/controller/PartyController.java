package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.response.PartyDetailDto;
import com.everyones.lawmaking.common.dto.response.PartyDetailResponse;
import com.everyones.lawmaking.facade.Facade;
import com.everyones.lawmaking.global.BaseResponse;
import com.everyones.lawmaking.service.PartyService;
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
import org.springframework.web.bind.annotation.*;

import static com.everyones.lawmaking.global.SwaggerConstants.EXAMPLE_ERROR_500_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/party")
@Tag(name = "정당 관련 API", description = "정당 관련 API 호출")
public class PartyController {
    private final Facade facade;
    private final PartyService partyService;
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
    @GetMapping("/detail")
    public BaseResponse<PartyDetailDto> getParty(@Parameter(example = "1", description = "정당 id")
                                                    @RequestParam("party_id") long partyId) {

        var response = facade.getPartyById(partyId);
        return BaseResponse.ok(response);
    }

    @Operation(summary = "정당 소속의 발의안 리스트 조회", description = "정당 의원이 발의한 법안 데이터 조회")
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
    @GetMapping("/bill")
    public BaseResponse<PartyDetailResponse> getBillsByParty(@Parameter(example = "1", description = "정당 id")
                   @RequestParam("party_id") long partyId,
                   @Parameter(example = "대표", description = "공동대표발의안 또는 대표발의 의안 조회 여부")
                   @Schema(type = "Boolean", allowableValues = {"true", "false"})
                   @RequestParam("is_represent") Boolean isRepresent,
                   @Parameter(example = "0", description = "스크롤할 때마다 page값을 0에서 1씩 늘려주면 됩니다.")
                   @RequestParam(name = "page", required = true) int page,
                   @Parameter(example = "3", description = "한번에 가져올 데이터 크기를 의미합니다.")
                   @RequestParam(name = "size", required = true) int size) {
        var pageable = PageRequest.of(page, size);
        var response = (isRepresent == true) ? facade.getRepresentativeBillsByParty(pageable, partyId)
                : facade.getPublicBillsByParty(pageable, partyId);

        return BaseResponse.ok(response);

    }

}
