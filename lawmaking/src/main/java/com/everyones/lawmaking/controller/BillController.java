package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.response.BillDetailDto;
import com.everyones.lawmaking.common.dto.response.MainFeedBillResponse;
import com.everyones.lawmaking.global.BaseResponse;
import com.everyones.lawmaking.service.BillService;
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
import org.springframework.web.bind.annotation.*;
import static com.everyones.lawmaking.global.SwaggerConstants.EXAMPLE_ERROR_500_CONTENT;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/bill")
@Tag(name = "법안 API", description = "법안 조회 API")
public class BillController {
    private final BillService billService;

    @Operation(summary = "메인피드 조회", description = "메인피드에 들어갈 데이터를 가져옵니다.")
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
    @GetMapping("/mainfeed")
    public BaseResponse<MainFeedBillResponse> getNext3Bills(
            @Parameter(example = "0", description = "스크롤할 때마다 page값을 0에서 1씩 늘려주면 됩니다.")
            @RequestParam(name = "page", required = true)
            int page,
            @Parameter(example = "3", description = "한번에 가져올 데이터 크기를 의미합니다.")
            int size
            ) {
        var pageable = PageRequest.of(page, size);
        var result = billService.getNext3Bills(page, pageable);
        return BaseResponse.ok(result);

    }

    @Operation(summary = "메인피드 단계로 조회", description = "메인피드에 들어갈 데이터를 단계를 통해 가져옵니다.")
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
    @GetMapping("/mainfeed/stage")
    public BaseResponse<MainFeedBillResponse> getNext3BillsWithStage(
            @Parameter(example = "0", description = "스크롤할 때마다 page값을 0에서 1씩 늘려주면 됩니다.")
            @RequestParam(name = "page", required = true) int page,
            @Parameter(example = "3", description = "한번에 가져올 데이터 크기를 의미합니다.")
            @RequestParam(name = "size", required = true) int size,
            @Parameter(example = "공포", description = "법안의 단계 현황을 나타냅니다.")
            @Schema(type = "string", allowableValues = {"접수",
                    "공포","위원회 심사","본회의 심의"})
            @RequestParam(name = "stage", required = true) String stage
            ) {
        var pageable = PageRequest.of(page, size);
        var result = billService.getNext3BillsWithStage(page, pageable, stage);
        return BaseResponse.ok(result);

    }

    @Operation(summary = "법안 상세 조회", description = "법안 id로 법안 데이터를 가져옵니다.")
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
    @GetMapping("/detail/{bill_id}")
    public BaseResponse<BillDetailDto> getBillWtihDeatail(
            @Parameter(example = "PRC_G2O3O1N2O1M1K1L5A0A8Z2Z2Y7W6X3")
            @PathVariable("bill_id") String billId) {
        var result = billService.getBillWtihDeatail(billId);
        return BaseResponse.ok(result);

    }
    @Operation(summary = "테스트 결과 작동하지 않습니다. 추후 삭제예정")
    @GetMapping("/search/summary")
    public BaseResponse<MainFeedBillResponse> searchBills(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "size", required = true) int size,
            @RequestParam(name = "summaryquestion", required = true) String summaryquestion,
            Pageable pageable) {

        var result = billService.searchBillsBySummary(page, pageable, summaryquestion);
        return BaseResponse.ok(result);

    }


}
