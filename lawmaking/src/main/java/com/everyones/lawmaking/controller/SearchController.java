package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.response.SearchDataResponse;
import com.everyones.lawmaking.facade.Facade;
import com.everyones.lawmaking.global.BaseResponse;
import com.everyones.lawmaking.global.error.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.global.error.ErrorCode;
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
@RequestMapping("/v1/search")
@Tag(name="검색 관련 API", description = "의원, 정당 및 법안 검색 API")
public class SearchController {

    private final Facade facade;

    // 의원, 정당 검색

    @Operation(summary = "의원 및 정당 검색", description = "의원 또는 정당 탭에서 검색한 결과를 주는 API입니다. ")
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
    @GetMapping("/congressman/party")
    public BaseResponse<SearchDataResponse> searchCongressmanAndParty (
            @Parameter(example = "국민", description = "국민의 힘 정당 노리는 검색어")
            @RequestParam("search_word") String searchWord,
            @Parameter(example = "0", description = "검색 결과 페이징을 위한 페이지 넘버")
            @RequestParam("page") int page) {

        if(searchWord == null || searchWord.trim().isEmpty()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        var result = facade.searchCongressmanAndParty(searchWord, page);
        return BaseResponse.ok(result);

    }

     //법안 검색
    @Operation(summary = "법안 검색", description = "법안을 검색한 결과를 주는 API입니다. ")
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
    public BaseResponse<SearchDataResponse> searchBill (
            @Parameter(example = "소방", description = "소방 관련 법안을 검색하려는 검색어")
            @RequestParam("search_word") String searchWord,
            @Parameter(example = "0", description = "검색 결과 페이징을 위한 페이지 넘버")
            @RequestParam("page") int page) {
        if (searchWord.trim().length()<2){
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        var result = facade.searchBill(searchWord,page);
        return BaseResponse.ok(result);

    }

    @Operation(summary = "후보자 검색", description = "후보자 검색한 결과를 주는 API입니다. ")
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
    @GetMapping("/candidate")
    public BaseResponse<SearchDataResponse> searchCandidate (
            @Parameter(example = "소방", description = "소방 관련 법안을 검색하려는 검색어")
            @RequestParam("search_word") String searchWord,
            @Parameter(example = "0", description = "검색 결과 페이징을 위한 페이지 넘버")
            @RequestParam("page") int page) {
        var pageable = PageRequest.of(page, 5);

        var result = facade.searchCandidate(searchWord,pageable);
        return BaseResponse.ok(result);

    }
}
