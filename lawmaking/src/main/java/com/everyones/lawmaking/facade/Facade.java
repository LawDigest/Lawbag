package com.everyones.lawmaking.facade;

import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.common.dto.CongressmanDto;
import com.everyones.lawmaking.common.dto.response.*;
import com.everyones.lawmaking.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class Facade {
    private final PartyService partyService;
    private final BillService billService;
    private final CongressmanService congressmanService;
    private final LikeService likeService;
    private final UserService userService;

    // default로 메인피드에 띄울 법안 가져오기 현재 최신순으로 반환해줌.
    public BillListResponse getBillsFromMainFeed(Pageable pageable) {
        return billService.getBillsByDefault(pageable);
    }

    // 법안의 처리 단계에 따라 법안 페이징해서 법안 반환
    public BillListResponse getBillsByStage(Pageable pageable, String stage) {
        return billService.getBillsByStage(pageable, stage);
    }

    // 법안 아이디를 기준으로, 하나의 법안 상세 조회
    public BillDto getBillByBillId(String billId) {
        return billService.getBillWithDetail(billId);
    }

    // 법안 조회수 증가
    public BillViewCountResponse updateViewCount(String billId) {
        return billService.updateViewCount(billId);
    }

    // Congressman 대표 발의 가져오기
    public BillListResponse getBillsFromRepresentativeProposer(String congressmanId, Pageable pageable) {
        return billService.getBillInfoFromRepresentativeProposer(congressmanId, pageable);
    }

    // Congressman 공동 발의 법안 가져오기
    public BillListResponse getBillsFromPublicProposer(String congressmanId, Pageable pageable) {
        return billService.getBillInfoFromPublicProposer(congressmanId, pageable);
    }

    // 의원 상세 조회
    public CongressmanDto getCongressman(String congressmanId) {
        return congressmanService.getCongressman(congressmanId);
    }

    // 정당 상세 조회
    public PartyDetailDto getPartyById(long partyId) {
        return partyService.getPartyById(partyId);
    }

    //
    public BillListResponse getRepresentativeBillsByParty(Pageable pageable, long partyId) {
        return billService.getRepresentativeBillsByParty(pageable, partyId);
    }

    public BillListResponse getPublicBillsByParty(Pageable pageable, long partyId) {
        return billService.getPublicBillsByParty(pageable, partyId);
    }

    // 법안 좋아요 기능
    public BillLikeResponse likeBill(long userId, String billId, boolean likeChecked) {
        // 해당하는 userId와 billId가 있는 지 확인 없으면 하위 모듈에서 에러 발생
        var bill = billService.getBillEntityById(billId);
        var user = userService.getUserId(userId);
        billService.updateBillLikeCount(bill, likeChecked);
        return likeService.likeBill(user, bill);
    }

    // 의원 좋아요 기능
    public CongressmanLikeResponse likeCongressman(long userId, String congressmanId, boolean likeChecked) {
        var user = userService.getUserId(userId);
        var congressman = congressmanService.findCongressman(congressmanId);
        congressmanService.updateCongressmanLikeCount(congressman, likeChecked);
        return likeService.likeCongressman(user, congressman);
    }





}
