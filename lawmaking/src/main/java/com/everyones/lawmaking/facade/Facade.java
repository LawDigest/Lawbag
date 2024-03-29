package com.everyones.lawmaking.facade;

import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.common.dto.response.*;
import com.everyones.lawmaking.domain.entity.ColumnEventType;
import com.everyones.lawmaking.domain.entity.User;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class Facade {
    private final PartyService partyService;
    private final BillService billService;
    private final CongressmanService congressmanService;
    private final LikeService likeService;
    private final UserService userService;
    private final NotificationService notificationService;

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
    public CongressmanResponse getCongressman(String congressmanId) {
        return congressmanService.getCongressman(congressmanId);
    }

    // 정당 상세 조회
    public PartyDetailResponse getPartyById(long partyId) {
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

    // 정당 팔로우 기능
    @Transactional
    public PartyFollowResponse followParty(long userId, long partyId, boolean followChecked) {
        var user = userService.getUserId(userId);
        var party = partyService.findParty(partyId);
        partyService.updatePartyFollowCount(party, followChecked);
        return likeService.followParty(user, party);
    }

    // 팔로우한 의원 조회
    public List<LikingCongressmanResponse> getLikingCongressman(long userId) {
        return congressmanService.getLikingCongressman(userId);
    }

    // 팔로우한 정당 조회
    public List<FollowingPartyResponse> getFollowingParty(long userId) {
        return partyService.getFollowingParty(userId);
    }

    // 마에 페이지 유저 정보 조회
    public UserMyPageInfoResponse getUserMyPageInfo(long userId) {
        return userService.getUserMyPageInfo(userId);
    }

    // 검색 정당 및 의원 조회
    public SearchDataResponse searchCongressmanAndParty(String searchWord, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        var searchDataReesponse = congressmanService.searchCongressman(searchWord, pageable);

        if (page == 0) {
            var searchPartyList = partyService.searchParty(searchWord);
            searchDataReesponse.setSearchResponse(Stream
                    .concat(searchDataReesponse.getSearchResponse().stream(), searchPartyList.stream())
                    .toList());
        }
        return searchDataReesponse;

    }

    // 검색 법안 조회
//    public List<SearchResponse> searchBill(String searchWord) {
//
//    }

    // 알림 조회
    public List<NotificationResponse> getNotifications(long userId){
        return notificationService.getNotifications(userId);
    }

    // 알림 읽음 처리
    public List<NotificationResponse> readNotifications(long userId) {
        return notificationService.readNotifications(userId);
    }

    // 알림 데이터를 각 테이블에 해당하는 실제 데이터로 변환 (ex : bill_id

    public List<String> rpInsert(List<String> raw) {
        var congressman = congressmanService.findCongressman(raw.get(0));
        var billRepProposer = congressman.getName();

        var bill = billService.getBillEntityById(raw.get(1));
        var billId = bill.getId();
        var billName = bill.getBillName();
        var billProposers = bill.getProposers();

        return List.of(billId, billRepProposer, billProposers, billName);
    }
    public List<String> updateBillStage(List<String> raw) {
        return raw;
    }

    public List<String> updateCongressmanParty(List<String> raw) {
        var congressman = congressmanService.findCongressman(raw.get(0));
        var congressmanId = congressman.getId();
        var congressmanName = congressman.getName();

        var party = partyService.findParty(Long.parseLong(raw.get(1)));
        var partyName = party.getName();

        return List.of(congressmanId, partyName, congressmanName);
    }

    public List<User> getSubscribedUsers(ColumnEventType cet, String targetId) {
        return switch (cet) {
            case RP_INSERT, CONGRESSMAN_PARTY_UPDATE -> likeService.getUserByLikedCongressmanId(targetId);
            case BILL_STAGE_UPDATE -> likeService.getUserByLikedBillId(targetId);
        };
    }
}
