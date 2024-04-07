package com.everyones.lawmaking.facade;

import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.common.dto.response.*;
import com.everyones.lawmaking.domain.entity.ColumnEventType;
import com.everyones.lawmaking.domain.entity.User;
import com.everyones.lawmaking.global.util.AuthenticationUtil;
import com.everyones.lawmaking.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@Slf4j
public class Facade {
    private final PartyService partyService;
    private final BillService billService;
    private final CongressmanService congressmanService;
    private final LikeService likeService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final PartyPromiseService partyPromiseService;
    private final ProportionalCandidateService proportionalCandidateService;
    private final DistrictCandidateService districtCandidateService;
    private final AuthService authService;
    private final DistrictService districtService;
    private final CandidateService candidateService;

    public BillListResponse getBillsFromMainFeed(Pageable pageable) {
        var billListResponse = billService.getBillsByDefault(pageable);
        return setBillListResponseBookMark(billListResponse);
    }

    // 법안의 처리 단계에 따라 법안 페이징해서 법안 반환
    public BillListResponse getBillsByStage(Pageable pageable, String stage) {
        var billListResponse = billService.getBillsByStage(pageable, stage);
        return setBillListResponseBookMark(billListResponse);
    }

    // 법안 아이디를 기준으로, 하나의 법안 상세 조회
    public BillDto getBillByBillId(String billId) {
        var billDto =  billService.getBillWithDetail(billId);
        var userId = AuthenticationUtil.getUserId();
        if (userId.isEmpty()) {
            return billDto;
        }
        var isBookMark = likeService.getBillLikeChecked(billDto.getBillInfoDto().getBillId(), userId.get());
        billDto.setIsBookMark(isBookMark);
        return billDto;
    }

    // 법안 조회수 증가
    public BillViewCountResponse updateViewCount(String billId) {
        return billService.updateViewCount(billId);
    }

    // Congressman 대표 발의 가져오기
    public BillListResponse getBillsFromRepresentativeProposer(String congressmanId, Pageable pageable) {
        var billListResponse = billService.getBillInfoFromRepresentativeProposer(congressmanId, pageable);
        return setBillListResponseBookMark(billListResponse);
    }

    // Congressman 공동 발의 법안 가져오기
    public BillListResponse getBillsFromPublicProposer(String congressmanId, Pageable pageable) {
        var billListResponse = billService.getBillInfoFromPublicProposer(congressmanId, pageable);
        return setBillListResponseBookMark(billListResponse);
    }

    // 의원 상세 조회
    public CongressmanResponse getCongressman(String congressmanId) {
        var congressmanResponse = congressmanService.getCongressman(congressmanId);
        var userId = AuthenticationUtil.getUserId();
        if (userId.isEmpty()) {
            return congressmanResponse;
        }
        var congressmanLike = likeService.getCongressmanLike(congressmanId, userId.get());
        congressmanResponse.setLikeChecked(congressmanLike);
        return congressmanResponse;
    }

    // 정당 상세 조회
    public PartyDetailResponse getPartyById(long partyId) {
        var partyDetailResponse =  partyService.getPartyById(partyId);
        var userId = AuthenticationUtil.getUserId();
        if (userId.isEmpty()) {
            return partyDetailResponse;
        }
        var followChecked = likeService.getFollowParty(partyId, userId.get());
        partyDetailResponse.setFollowed(followChecked);
        return partyDetailResponse;
    }

    //
    public BillListResponse getRepresentativeBillsByParty(Pageable pageable, long partyId) {
        var billListResponse = billService.getRepresentativeBillsByParty(pageable, partyId);
        return setBillListResponseBookMark(billListResponse);
    }

    public BillListResponse getPublicBillsByParty(Pageable pageable, long partyId) {
        var billListResponse = billService.getPublicBillsByParty(pageable, partyId);
        return setBillListResponseBookMark(billListResponse);
    }

    public PartyCongressmanResponse getPartyCongressman(long partyId, Pageable pageable) {
        return congressmanService.getPartyCongressman(partyId, pageable);
    }

    // 법안 좋아요 기능
    @Transactional
    public BillLikeResponse likeBill(long userId, String billId, boolean likeChecked) {
        var user = userService.findById(userId);
        var bill = billService.findById(billId);
        billService.updateBillLikeCount(bill, likeChecked);
        return likeService.likeBill(user, bill, likeChecked);
    }

    // 의원 좋아요 기능
    @Transactional
    public CongressmanLikeResponse likeCongressman(long userId, String congressmanId, boolean likeChecked) {
        var user = userService.findById(userId);
        var congressman = congressmanService.findById(congressmanId);
        congressmanService.updateCongressmanLikeCount(congressman, likeChecked);
        return likeService.likeCongressman(user, congressman, likeChecked);
    }

    // 정당 팔로우 기능
    @Transactional
    public PartyFollowResponse followParty(long userId, long partyId, boolean followChecked) {
        var user = userService.findById(userId);
        var party = partyService.findById(partyId);
        partyService.updatePartyFollowCount(party, followChecked);
        return likeService.followParty(user, party, followChecked);
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
        var searchDataResponse = congressmanService.searchCongressman(searchWord, pageable);

        if (page == 0) {
            var searchPartyList = partyService.searchParty(searchWord);
            searchDataResponse.setSearchResponse(Stream
                    .concat(searchDataResponse.getSearchResponse().stream(), searchPartyList.stream())
                    .toList());
        }
        return searchDataResponse;

    }

    // 검색 법안 조회
    public SearchDataResponse searchBill(String searchWord,int page) {

        Pageable pageable = PageRequest.of(page, 5);

        return billService.searchBill(searchWord, pageable);

    }

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
        var congressman = congressmanService.findById(raw.get(0));
        var billRepProposer = congressman.getName();

        var bill = billService.findById(raw.get(1));
        var billId = bill.getId();
        var billName = bill.getBillName();
        var billProposers = bill.getProposers();

        return List.of(billId, billRepProposer, billProposers, billName,congressman.getParty().getPartyImageUrl());
    }
    public List<String> updateBillStage(List<String> raw) {
        var party = partyService.getPartyByBillId(raw.get(0));
        raw.add(party.getPartyImageUrl());
        return raw;
    }

    public List<String> updateCongressmanParty(List<String> raw) {
        var congressman = congressmanService.findById(raw.get(0));
        var congressmanId = congressman.getId();
        var congressmanName = congressman.getName();

        var party = partyService.findById(Long.parseLong(raw.get(1)));
        var partyName = party.getName();

        return List.of(congressmanId, partyName, congressmanName,congressman.getParty().getPartyImageUrl());
    }

    public List<User> getSubscribedUsers(ColumnEventType cet, String targetId) {
        return switch (cet) {
            case RP_INSERT, CONGRESSMAN_PARTY_UPDATE -> userService.getUserByLikedCongressmanId(targetId);
            case BILL_STAGE_UPDATE -> userService.getUserByLikedBillId(targetId);
        };
    }

    private BillListResponse setBillListResponseBookMark(BillListResponse billListResponse) {
        var userId = AuthenticationUtil.getUserId();
        if (userId.isEmpty()) {
            return billListResponse;
        }
        var billList = billListResponse.getBillList().stream()
                .map(billDto -> {
                    var isBookMark = likeService.getBillLikeChecked(billDto.getBillInfoDto().getBillId(), userId.get());
                    billDto.setIsBookMark(isBookMark);
                    return billDto;
                })
                .toList();
        billListResponse.setBillList(billList);
        return billListResponse;
    }

    // 정당 공약 조회
    public PartyPromiseResponse getPartyPromise(long partyId, Pageable pageable) {

        return partyPromiseService.getPartyPromise(partyId, pageable);
    }

    public ProportionalCandidateListResponse getProportionalCandidateList(long partyId, Pageable pageable){
        return proportionalCandidateService.getProportionalCandidateList(partyId, pageable);
    }



    public ProportionalPartyImageListResponse getProPartyImage(Pageable pageable){
        return partyService.getProportionalPartyImageList(pageable);
    }

    public CandidateDetailResponse proportionalCandidateDetail(long candidateId){
        return     proportionalCandidateService.getProportionalCandidateDetail(candidateId);

    }

    public CandidateDetailResponse districtCandidateDetail(long candidateId){
        return     districtCandidateService.getDistrictCandidateDetail(candidateId);

    }

public WithdrawResponse withdraw(String userId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
    return authService.withdraw(userId,httpRequest, httpResponse);

    }

    public DistrictCandidateListResponse getDistrictCandidateList(long districtId, Pageable pageable){
        return districtCandidateService.getDistrictCandidateList(districtId, pageable);
    }

    public List<DistrictResponse> getDistrictList(String cityName, String guName){

        if(cityName != null && !cityName.isEmpty()){
            // 도시이름 받음
            if(guName != null && !guName.isEmpty()){
            // 도시이름이랑 지역구 받음
                return districtService.getDistrictList(cityName,guName);
            }
            else{
                //도시 이름만 받음
                return districtService.getGuNameList(cityName);
            }
        }
        else{
            // 아무것도 안받음.
            return districtService.getAllCity();

        }

}
    public SearchDataResponse searchCandidate(String searchWord,Pageable pageable ) {

        return candidateService.searchCandidate(searchWord, pageable);

    }

    public ProportionalPartyResponse getProPartyInfo(long partyId) {

        return partyService.getProPartyInfo(partyId);

    }



}
