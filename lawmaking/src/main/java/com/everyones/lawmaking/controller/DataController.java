package com.everyones.lawmaking.controller;


import com.everyones.lawmaking.common.dto.request.BillListDfRequest;
import com.everyones.lawmaking.common.dto.request.BillResultListDfRequest;
import com.everyones.lawmaking.common.dto.request.BillStageListDfRequest;
import com.everyones.lawmaking.common.dto.request.LawmakerListDfRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.everyones.lawmaking.global.SwaggerConstants.EXAMPLE_ERROR_500_CONTENT;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auto_data_upload")
@Tag(name = "내수용 데이터 API", description = "새로 수집되는 데이터들을 기록하기 위한 API")
public class DataController {


    private final Facade facade;

    @Operation(summary = "발의법률안 삽입 요청 API", description = "수집한 발의법률안 삽입 요청한다.")
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
    @PostMapping("/bill")
    public BaseResponse<String> insertBill(
            @Parameter(description = "발의법률안 삽입 데이터", required = true)
            @RequestBody BillListDfRequest billListDfRequest) {
        System.out.println(billListDfRequest);
        var billDfRequestList = billListDfRequest.getBillRequestList();
        facade.insertBillInfoDf(billDfRequestList);

        return BaseResponse.ok("발의법률안정보 삽입 데이터 요청 성공");

    }



    @Operation(summary = "날짜별 법안 처리상태 변동 수집 데이터 수정 요청 API", description = "날짜별 법안 처리상태 변동 수집 데이터 수정 요청한다.")
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
    @PostMapping("/bill_stage")
    public BaseResponse<List<Long>> updateBillStageDf(
            @Parameter(description = "날짜별 법안 처리상태 변동 수집 수정 데이터", required = true)
            @RequestBody BillStageListDfRequest billStageListDfRequest) {
        var billStageDfRequestList = billStageListDfRequest.getBillStageRequestList();
        var result = facade.updateBillStageDf(billStageDfRequestList);

        return BaseResponse.ok(result);

    }


    @Operation(summary = "당일 법안 본회의 처리 결과 수집 수정 요청 API", description = "당일 법안 본회의 처리 결과 수집 수정 요청한다.")
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
    @PostMapping("/bill_result")
    public BaseResponse<String> updateBillResultDf(
            @Parameter(description = "당일 법안 본회의 처리 결과 수집 수정 데이터", required = true)
            @RequestBody BillResultListDfRequest billResultListDfRequest) {
        var billDfRequestList=billResultListDfRequest.getBillResultDfRequestList();
        facade.updateBillResultDf(billDfRequestList);
        return BaseResponse.ok("200");

    }

    @Operation(summary = "국회의원 정보 수집 수정 요청 API", description = "국회의원 정보 수집 수정 요청한다.")
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
    @PostMapping("/lawmaker")
    public BaseResponse<String> updateLawmakerDf(
            @Parameter(description = "국회의원 정보 수집 삽입 api", required = true)
            @RequestBody LawmakerListDfRequest lawmakerListDfRequest) {
        var lawmakerDfRequestList=lawmakerListDfRequest.getLawmakerDfRequestList();

        facade.updateLawmakerDf(lawmakerDfRequestList);
        return BaseResponse.ok("200");

    }
    //@Todo Bill의 새로운 컬럼(PROC_RESULT_CD) 추가 후 -> bill_vote, bill_vote/individual



//@Todo 새로 추가되는 타임라인 기능 -> bill_vote, bill_vote/individual
//
//    @Operation(summary = "당일 본회의 투표수 수집 삽입 요청 API", description = "당일 본회의 투표수 수집 삽입 요청한다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "조회 성공"),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "서버 오류 (문제 지속시 BE팀 문의)",
//                    content = {@Content(
//                            mediaType = "application/json;charset=UTF-8",
//                            schema = @Schema(implementation = BaseResponse.class),
//                            examples = @ExampleObject(value = EXAMPLE_ERROR_500_CONTENT)
//                    )}
//            ),
//    })
//    @PostMapping("/bill_vote")
//    public BaseResponse<String> getCongressmanDetails(
//            @Parameter(description = "당일 본회의 투표수 수집 삽입 요청 데이터", required = true)
//            @RequestBody BillInfoListRequest  billInfoListRequest) {
//        log.debug("data={}", billInfoListRequest.getBillInfoRequestList().get(0).getBillId());
//        return BaseResponse.ok("200");
//
//    }
//
//    @Operation(summary = "개별 국회의원별 찬성, 반대 표결 결과 수집 삽입 요청 API", description = "개별 국회의원별 찬성, 반대 표결 결과 수집 삽입 요청한다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "조회 성공"),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "서버 오류 (문제 지속시 BE팀 문의)",
//                    content = {@Content(
//                            mediaType = "application/json;charset=UTF-8",
//                            schema = @Schema(implementation = BaseResponse.class),
//                            examples = @ExampleObject(value = EXAMPLE_ERROR_500_CONTENT)
//                    )}
//            ),
//    })
//    @PostMapping("/bill_vote/individual")
//    public BaseResponse<String> getCongressmanDetails(
//            @Parameter(description = "개별 국회의원별 찬성, 반대 표결 결과 수집 삽입 api", required = true)
//            @RequestBody BillInfoListRequest  billInfoListRequest) {
//        log.debug("data={}", billInfoListRequest.getBillInfoRequestList().get(0).getBillId());
//        return BaseResponse.ok("200");
//
//    }
//


}
