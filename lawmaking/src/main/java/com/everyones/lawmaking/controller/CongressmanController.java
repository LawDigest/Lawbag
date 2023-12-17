package com.everyones.lawmaking.controller;


import com.everyones.lawmaking.common.dto.response.CongressmanDetailResponse;

import com.everyones.lawmaking.service.CongressmanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

//의원 자세히 보기 api
//특정 의원이 대표 발의 법안 api
//특정 의원이 공동 발의 법안 api
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/congressman")
@Tag(name="Congressman Controller", description = "의원 상세 페이지")
public class CongressmanController {
    private final CongressmanService congressmanService;
    @GetMapping("/detail")
    public ResponseEntity<Map<String, Object>> getCongressmanBillsDetails(
            @RequestParam("congressman_id") String congressmanId,
            @RequestParam("stage") String stage, // "represent" or "public"
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Map<String, Object> response;

        if ("대표".equals(stage)) {
            response = congressmanService.getCongressmanDetails(congressmanId, pageable);
        } else if ("공동".equals(stage)) {
            response = congressmanService.getCongressmanCoSponsorshipDetails(congressmanId, pageable);
        } else {
            // 유효하지 않은 stage 값 처리
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/detail/represent")
//    public ResponseEntity<CongressmanDetailResponse> getCongressmanDetails(
//            @RequestParam("congressman_id") String congressmanId,
//            @RequestParam("page") int page,
//            @RequestParam("size") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        CongressmanDetailResponse response = congressmanService.getCongressmanDetails(congressmanId, pageable);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/detail/public")
//    public ResponseEntity<CongressmanDetailResponse> getCongressmanCoSponsorshipDetails(
//            @RequestParam("congressman_id") String congressmanId,
//            @RequestParam("page") int page,
//            @RequestParam("size") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        CongressmanDetailResponse response = congressmanService.getCongressmanCoSponsorshipDetails(congressmanId, pageable);
//        return ResponseEntity.ok(response);
//    }
////    // commits, 최신순 정렬 해결 필요
//    @GetMapping("/detail/{congressman_id}/represent")
//    public ResponseEntity<CongressmanDetailResponse> getCongressmanDetails(
//            @PathVariable("congressman_id") String congressmanId,
//            @RequestParam("page") int page,
//            @RequestParam("size") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        CongressmanDetailResponse response = congressmanService.getCongressmanDetails(congressmanId, pageable);
//        return ResponseEntity.ok(response);
//    }
//    @GetMapping("/detail/{congressman_id}/public")
//    public ResponseEntity<CongressmanDetailResponse> getCongressmanCoSponsorshipDetails(
//            @PathVariable("congressman_id") String congressmanId,
//            @RequestParam("page") int page,
//            @RequestParam("size") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        CongressmanDetailResponse response = congressmanService.getCongressmanCoSponsorshipDetails(congressmanId, pageable);
//        return ResponseEntity.ok(response);
//    }
}
//15줄부터 28줄에 적용되어있는 생성자는 CongressmanDto의 모든 필드를 인자로 넘겨주는 생성자입니다. 위에 주석 표시한 @AllArgsConstructor와 같은 기능으로, 해당 부분을 지우고 @AllArgsConstructor를 쓰는게 더 깔끔해보입니다.
// return CongressDetailBillDto.builder()
//         .billId(bill.getId())
//         .billName(bill.getBillName())
//         .proposeDate(bill.getProposeDate())
//         .proposers(bill.getProposers())
//         .summary(bill.getSummary())
//         .gptSummary(bill.getGptSummary())
//         .representProposer(representativeProposer.getCongressman().getName())
//         .representProposerId(representativeProposer.getCongressman().getId())
//         .representProposerParty(representativeProposer.getCongressman().getParty().getName())
//         .representProposerPartyId(representativeProposer.getCongressman().getParty().getId())
//         .representProposerImgUrl(representativeProposer.getCongressman().getParty().getPartyImageUrl())
//         .partyList(partyNames)
//         .partyIdList(partyIds)
//         .build();
//         위 부분을 dto 안에 static builder로 선언하여 생성자 주입하는 방식이 좋아보입니다. 근데 사실 일단 작동하는 코드니까 이부분은 그냥 냅두셔도 될 것 같습니다. 고생하셨씁니다.
//
