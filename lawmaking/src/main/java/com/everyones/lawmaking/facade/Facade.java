package com.everyones.lawmaking.facade;

import com.everyones.lawmaking.common.dto.*;
import com.everyones.lawmaking.common.dto.bill.BillDto;
import com.everyones.lawmaking.common.dto.request.*;
import com.everyones.lawmaking.common.dto.response.*;
import com.everyones.lawmaking.domain.entity.ColumnEventType;
import com.everyones.lawmaking.domain.entity.ProposerKindType;
import com.everyones.lawmaking.domain.entity.User;
import com.everyones.lawmaking.global.error.AuthException;
import com.everyones.lawmaking.global.error.ErrorCode;
import com.everyones.lawmaking.global.error.ExternalException;
import com.everyones.lawmaking.global.error.UserException;
import com.everyones.lawmaking.global.service.TokenService;
import com.everyones.lawmaking.global.util.AuthenticationUtil;
import com.everyones.lawmaking.global.util.NullUtil;
import com.everyones.lawmaking.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDate;
import java.util.*;
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
    private final AuthService authService;
    private final DataService dataService;
    private final RedisService redisService;
    private final RepresentativeProposerService representativeProposerService;
    private final BillProposerService billProposerService;
    private final BillTimelineService billTimelineService;
    private final VotePartyService votePartyService;
    private final VoteRecordService voteRecordService;
    private final SearchKeywordService searchKeywordService;
    private final OAuthService oAuthService;
    private final TokenService tokenService;
    private final TransactionTemplate transactionTemplate;

    // 법안 아이디를 기준으로, 하나의 법안 상세 조회
    public BillDetailResponse getBillByBillId(String billId) {
        var billDto =  billService.getBillWithDetail(billId);
        var userId = AuthenticationUtil.getUserId();
        var voteRecord = voteRecordService.getVoteRecordByBillId(billId);
        var votePartyList = votePartyService.getVotePartyListWithPartyByBillId(billId).stream()
                .map(PartyVoteDto::from)
                .toList();
        var voteResultResponse = VoteResultResponse.of(voteRecord, votePartyList);
        billDto.setVoteResultResponse(voteResultResponse);
        if (userId.isEmpty()) {
            return billDto;
        }
        var isBookMark = likeService.getBillLikeChecked(billDto.getBillInfoDto().getBillId(), userId.get());
        billDto.setIsBookMark(isBookMark);

        return billDto;
    }

    // 법안 조회수 증가
    public BillViewCountResponse updateViewCount(String billId) {
        redisService.addToViewsQueue(billId);
        return billService.updateViewCount(billId);
    }

    // Congressman 대표 발의 가져오기
    public BillListResponse getBillsFromRepresentativeProposer(String congressmanId, Pageable pageable, String stage) {
        var billListResponse = stage == null ? billService.getBillInfoFromRepresentativeProposer(congressmanId, pageable)
                : billService.getBillInfoFromRepresentativeProposer(congressmanId, pageable, stage);
        return setBillListResponseBookMark(billListResponse);
    }


    // Congressman 공동 발의 법안 가져오기
    public BillListResponse getBillsFromPublicProposer(String congressmanId, Pageable pageable, String stage) {
        var billListResponse = stage == null ? billService.getBillInfoFromPublicProposer(congressmanId, pageable)
                : billService.getBillInfoFromPublicProposer(congressmanId, pageable, stage);
        return setBillListResponseBookMark(billListResponse);
    }

    // 의원 상세 조회
    public CongressmanResponse getCongressman(String congressmanId) {
        var congressman = congressmanService.getCongressman(congressmanId);
        var followCount = likeService.getCongressmanFollowerCount(congressmanId);
        var representCount = representativeProposerService.countBillByCongressmanAsPublicProposer(congressmanId);
        var publicCount = billProposerService.countBillByCongressmanAsPublicProposer(congressmanId);
        var userId = AuthenticationUtil.getUserId();
        if (userId.isEmpty()) {
            return CongressmanResponse.of(congressman, followCount, representCount, publicCount);
        }
        var congressmanLike = likeService.getCongressmanLike(congressmanId, userId.get());
        return CongressmanResponse.of(congressman, followCount, representCount, publicCount, congressmanLike);
    }
    public CountDto getCongressmanLikeCount() {
        var userId = AuthenticationUtil.getUserId();
        if (userId.isEmpty()) {
            throw new AuthException.AuthInfoNotFound();
        }
        return likeService.getCongressmanLikeCount(userId.get());
    }

    // 정당 상세 조회
    public PartyDetailResponse getPartyById(long partyId) {
        var partyDetailResponse =  partyService.getPartyById(partyId);
        var totalCongressmanCount = congressmanService.getTotalCongressmanState(true);
        partyDetailResponse.setTotalCongressmanCount(totalCongressmanCount);
        var userId = AuthenticationUtil.getUserId();
        if (userId.isEmpty()) {
            return partyDetailResponse;
        }
        var followChecked = likeService.getFollowParty(partyId, userId.get());
        partyDetailResponse.setFollowed(followChecked);
        return partyDetailResponse;
    }

    //
    public BillListResponse getRepresentativeBillsByParty(Pageable pageable, long partyId, String stage) {
        var billListResponse = stage == null ? billService.getRepresentativeBillsByParty(pageable, partyId)
                : billService.getRepresentativeBillsByParty(pageable, partyId, stage);
        return setBillListResponseBookMark(billListResponse);
    }

    public BillListResponse getPublicBillsByParty(Pageable pageable, long partyId, String stage) {
        var billListResponse = stage == null ? billService.getPublicBillsByParty(pageable, partyId)
                : billService.getPublicBillsByParty(pageable, partyId, stage);
        return setBillListResponseBookMark(billListResponse);
    }

    public PartyCongressmanResponse getPartyCongressman(long partyId) {
        return congressmanService.getPartyCongressman(partyId);
    }

    // 법안 좋아요 기능
    @Transactional
    public BillLikeResponse likeBill(long userId, String billId, boolean likeChecked) {
        var user = userService.findById(userId);
        var bill = billService.findById(billId);
        if (likeChecked) {
            redisService.addToLikesQueue(billId);
        }
        return likeService.likeBill(user, bill, likeChecked);
    }

    // 의원 좋아요 기능
    @Transactional
    public CongressmanLikeResponse likeCongressman(long userId, String congressmanId, boolean likeChecked) {
        var user = userService.findById(userId);
        var congressman = congressmanService.findById(congressmanId);
        return likeService.likeCongressman(user, congressman, likeChecked);
    }

    // 정당 팔로우 기능
    @Transactional
    public PartyFollowResponse followParty(long userId, long partyId, boolean followChecked) {
        var user = userService.findById(userId);
        var party = partyService.findById(partyId);
        return likeService.followParty(user, party, followChecked);
    }

    // 팔로우한 의원 조회
    public List<LikingCongressmanResponse> getLikingCongressman(long userId) {
        return congressmanService.getLikingCongressman(userId);
    }
    // 북마크한 법안 조회
    // 좋아요를 한 날짜 순서대로 조회하는게 맞나? 근데 그러면 너무 로직이 복잡해짐...
    public BillListResponse getBookmarkedBills(Pageable pageable, long userId) {
        var billListResponse = billService.getBookmarkingBills(pageable, userId);
        return setBillListResponseBookMark(billListResponse);
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
    public SearchDataResponse searchCongressmanAndParty(String searchWord) {
        var searchDataResponse = congressmanService.searchCongressman(searchWord);
            var searchPartyList = partyService.searchParty(searchWord);
            searchDataResponse.setSearchResponse(Stream
                    .concat(searchDataResponse.getSearchResponse().stream(), searchPartyList.stream())
                    .toList());
        return searchDataResponse;

    }

    public CountDto getBookmarkedBillCount() {
        var userId = AuthenticationUtil.getUserId();
        return likeService.getBookmarkedBillCount(userId.get());
    }

    // 검색 법안 조회
    public SearchBillResponse searchBill(String searchWord,int page) {

        Pageable pageable = PageRequest.of(page, 5);
        var billList = billService.searchBill(searchWord, pageable);
        var billListByUser = setBillListResponseBookMark(billList);

        return SearchBillResponse.builder()
                .paginationResponse(billList.getPaginationResponse())
                .searchResponse(billListByUser.getBillList())
                .build();
    }

    // 알림 조회
    public List<NotificationResponse> getNotifications(long userId){
        return notificationService.getNotifications(userId);
    }

    // 알림 읽음 처리
    public List<NotificationResponse> readAllNotifications(long userId) {
        return notificationService.readNotification(userId, Optional.empty());
    }

    public List<NotificationResponse> readNotification(long userId, int notificationId) {
        return notificationService.readNotification(userId, Optional.of(notificationId));
    }

    public String deleteAllNotification(long userId) {
        return notificationService.deleteAllNotifications(userId);
    }

    public String deleteNotification(long userId, int notificationId) {
        return notificationService.deleteNotification(userId, notificationId);
    }

    public NotificationCountResponse countNotifications(long userId) {
        return notificationService.countNotifications(userId);
    }





    // 알림 데이터를 각 테이블에 해당하는 실제 데이터로 변환 (ex : bill_id
    public List<String> insertRepresentativeBill(List<String> uniqueKeys) {
        var bill = billService.findById(uniqueKeys.get(1));
        if (!(bill.getProposerKind()==(ProposerKindType.CONGRESSMAN))) {
            return List.of();
        }
        var congressman = congressmanService.findById(uniqueKeys.get(0));
        var congressmanName = congressman.getName();
        var billId = bill.getId();
        var billBriefSummary = bill.getBriefSummary();
        String partyName = NullUtil.nullCoalescing(() -> congressman.getParty().getName(), "defaultPartyName");
        var congressmanInfo = partyName+":"+congressman.getCongressmanImageUrl();


        return List.of(billId, congressmanName, billBriefSummary, congressmanInfo);
    }


    public List<String> updateBillStage(List<String> uniqueKeys) {
        var billId = uniqueKeys.get(0);
        var bill = billService.findById(billId);
        var proposerKind = bill.getProposerKind();
        var briefSummary = bill.getBriefSummary();
        var proposers = bill.getProposers();
        var stage = bill.getStage();

        if(proposerKind.equals(ProposerKindType.CONGRESSMAN)){
            return Stream.concat(Stream.of(billId, briefSummary, stage, proposerKind.name()), partyService.getPartyInfoList(billId).stream())
                    .toList();
        }
        else{
            return List.of(billId, briefSummary, stage, proposerKind.name(),proposers);

        }
    }

    public List<String> updateCongressmanParty(List<String> uniqueKeys) {
        var congressman = congressmanService.findById(uniqueKeys.get(0));
        var congressmanId = congressman.getId();
        var congressmanName = congressman.getName();

        String partyName = NullUtil.nullCoalescing(() -> congressman.getParty().getName(), "defaultPartyName");
        var congressmanInfo = partyName+":"+congressman.getCongressmanImageUrl();
        return List.of(congressmanId, partyName, congressmanName,congressmanInfo);
    }

    public List<String> updateBillResult(List<String> relatedEntityIds) {
        var billId = relatedEntityIds.get(0);
        var bill = billService.findById(billId);
        var proposerKind = bill.getProposerKind();
        var briefSummary = bill.getBriefSummary();
        var proposers = bill.getProposers();
        var billResult = bill.getBillResult();

        if(proposerKind.equals(ProposerKindType.CONGRESSMAN)){
            return Stream.concat(Stream.of(billId, briefSummary, billResult, proposerKind.name()), partyService.getPartyInfoList(billId).stream())
                    .toList();
        }
        else{
            return List.of(billId, briefSummary, billResult, proposerKind.name(), proposers);

        }
    }

    public List<String> getProcessedData(ColumnEventType columnEventType,List<String> eventData) {
        return switch (columnEventType) {
            case RP_INSERT -> insertRepresentativeBill(eventData);
            case CONGRESSMAN_PARTY_UPDATE -> updateCongressmanParty(eventData);
            case BILL_STAGE_UPDATE -> updateBillStage(eventData);
            case BILL_RESULT_UPDATE -> updateBillResult(eventData);
        };

    }

    public List<User> getSubscribedUsers(ColumnEventType cet, String targetId) {
        return switch (cet) {
            case RP_INSERT, CONGRESSMAN_PARTY_UPDATE -> userService.getUserByLikedCongressmanId(targetId);
            case BILL_STAGE_UPDATE, BILL_RESULT_UPDATE -> userService.getUserByLikedBillId(targetId);
        };
    }

    // TODO: 가져가서 제거할 생각하기
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



    public WithdrawResponse withdraw(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        var userId = AuthenticationUtil.getUserId()
                .orElseThrow(UserException.UserNotFoundException::new);

        var authInfo = authService.getAuthInfo(userId);
        var socialId = authInfo.getSocialId();
        var provider = authInfo.getProvider().name();

        return transactionTemplate.execute(status -> {
            try {
                // 1. 로그아웃 및 리프레시 토큰 삭제
                tokenService.logout(httpRequest, httpResponse);

                // 2. 데이터 삭제
                deleteUserAccount(userId, socialId);

                // 3. 외부 API 호출 - unlink
                var oauthRefreshAccessTokenResponse = oAuthService.refreshAccessToken(provider, socialId);
                var accessToken = Objects.requireNonNull(oauthRefreshAccessTokenResponse.getBody()).getAccessToken();
                status.flush();
                oAuthService.unlink(socialId, accessToken);

                // 모든 작업이 성공적으로 완료되었을 때만 WithdrawResponse 반환
                return WithdrawResponse.of(authInfo);

            }
            catch (AuthException | UserException e) {
                // 인증 정보 삭제 실패 시 롤백 설정
                status.setRollbackOnly();
                throw e;
            }
            catch (HttpClientErrorException | HttpServerErrorException e) {
                // 외부 API 호출 실패 시 롤백 설정
                status.setRollbackOnly();
                String kakaoErrorMessage = e.getResponseBodyAsString();
                throw new ExternalException.ApiException(
                        ErrorCode.EXTERNAL_API_ERROR,
                        Map.of("error Message from social service", kakaoErrorMessage)
                );
            } catch (Exception e) {
                // 기타 예외 발생 시 트랜잭션 롤백
                status.setRollbackOnly();
                log.error("Error during user account deletion or unlink", e);
                throw new UserException.WithdrawalFailureException(Map.of("userId", String.valueOf(userId)));
            }
        });
    }
    public void deleteUserAccount(Long userId, String socialId) {
        try{


            // 1. 사용자의 모든 좋아요 데이터 삭제
        likeService.deleteBillLikeByUserId(userId);
        likeService.deleteCongressmanLikeByUserId(userId);
        likeService.deletePartyFollowByUserId(userId);

//         2. 알림, 검색어 삭제
            notificationService.deleteNotificationByUserId(userId);
            searchKeywordService.deleteAllSearchWordsByUserId(userId);

            // 3. 사용자 삭제
            int isUserDeleted = userService.deleteUserById(userId);
            if (isUserDeleted == 0) {
                throw new UserException.UserNotFoundException(Map.of("userId", String.valueOf(userId)));
            }

            // 4. 인증 정보 삭제
            int isAuthInfoDeleted = authService.deleteAuthInfoBySocialId(socialId);
            if (isAuthInfoDeleted == 0) {
                throw new AuthException.AuthInfoNotFound(Map.of("userId", String.valueOf(userId)));
            }
        }
        catch (Exception e) {
            log.error("Error during user account deletion", e);

            throw e;
        }

    }


    public void reissueToken(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
        authService.reissueToken(httpServletRequest, httpServletResponse);
    }

    public void insertBillInfoDf(List<BillDfRequest> billDfRequestList) {
        dataService.insertBillInfoDf(billDfRequestList);
    }


    public Map<String, List<String>> updateBillStageDf(List<BillStageDfRequest> billStageDfRequestList) {
        // list 길이가 1이상이면
        return dataService.updateBillStageDf(billStageDfRequestList);
    }

    public void updateBillResultDf(List<BillResultDfRequest> billResultDfRequestList) {
        // list 길이가 1이상이면
        dataService.updateBillResultDf(billResultDfRequestList);
    }

    public void updateLawmakerDf(List<LawmakerDfRequest> lawmakerDfRequestList) {
        dataService.updateLawmakerDf(lawmakerDfRequestList);
    }

    public List<String> insertAssemblyVote(List<VoteDfRequest> voteDfRequestList){
        return dataService.insertAssemblyVote(voteDfRequestList);

    }

    public List<String> insertVoteIndividual(List<VotePartyRequest> votePartyRequestList){
        return dataService.insertVoteParty(votePartyRequestList);

    }

    public void updateCongressmanCountByParty(){
        dataService.updateCongressmanCountByParty();
    }
    public void updateBillCountByParty(){
        dataService.updateBillCountByParty();
    }

    public void updateProposeDateByCongressman(){
        dataService.updateProposeDateByCongressman();
    }




    public List<BillDto> getPopularBills() {
        var popularBillIds = redisService.getPopularBills();
        var billList = billService.getBillListResponse(popularBillIds);
        var userIdOptional = AuthenticationUtil.getUserId();

        return userIdOptional.map(userId -> billList.stream()
                .map(billDto -> {
                    var isBookMark = likeService.getBillLikeChecked(billDto.getBillInfoDto().getBillId(), userId);
                    billDto.setIsBookMark(isBookMark);
                    return billDto;
                })
                .toList()).orElse(billList);
    }

    // 팔로우한 의원들의 대표발의안을 최신순으로 페이징해서 가져옴
    public BillListResponse getBillsFromFollowingTab(Pageable pageable) {
        var billList =  billService.findByUserAndCongressmanLike(pageable);
        return setBillListResponseBookMark(billList);
    }
    @Transactional(readOnly = true)
    public List<BillTimelineResponse> getTimeline(Pageable pageable) {
        var timelineDateList = billTimelineService.getDatePaging(pageable);
        List<BillTimelineResponse> timelineList = new ArrayList<>();
        for (LocalDate timelineDate : timelineDateList) {
            timelineList.add(getTimeline(timelineDate));
        }
        return timelineList;
    }
    @Transactional(readOnly = true)
    public BillTimelineResponse getTimeline(LocalDate proposeDate) {
        if (redisService.isTimelineCached(proposeDate)) {
            return redisService.getTimelineCached(proposeDate);
        }
        var submittedBills = getSubmittedBills(proposeDate);
        var plenaryBills  = getPlenaryBills(proposeDate);
        var promulgationBills = getPromulgationBills(proposeDate);
        var committeeBills = getCommitteeBills(proposeDate);
        return BillTimelineResponse.of(proposeDate, submittedBills,plenaryBills, promulgationBills, committeeBills);
    }
    public List<BillOutlineDto> getSubmittedBills(LocalDate proposeDate) {
        var submittedBillIds = billTimelineService.getSubmittedBillIds(proposeDate);
        var bills = billService.findBillsWithPartiesByIds(submittedBillIds);
        return bills.stream()
                .map(BillOutlineDto::from)
                .toList();
    }
    public List<BillOutlineDto> getPromulgationBills(LocalDate localDate) {
        var promulgationBillIds = billTimelineService.findPromulgationBillIds(localDate);
        var bills = billService.findBillsWithPartiesByIds(promulgationBillIds);
        return bills.stream()
                .map(BillOutlineDto::from)
                .toList();
    }
    public List<PlenaryDto> getPlenaryBills(LocalDate localDate) {
        var plenaryBillIds = billTimelineService.findPlenaryBillIds(localDate);
        var plenaryBills = new ArrayList<PlenaryDto>();
        billService.findBillsWithPartiesByIds(plenaryBillIds)
                .forEach(bill -> {
                    var voteRecord = voteRecordService.getVoteRecordByBillId(bill.getId());
                    var votePartyList = votePartyService.getVotePartyListWithPartyByBillId(bill.getId()).stream()
                            .map(PartyVoteDto::from)

                            .toList();
                    var plenaryBill = PlenaryDto.of(bill, voteRecord, votePartyList);
                    plenaryBills.add(plenaryBill);
                });
        return plenaryBills;
    }

    public List<CommitteeAuditDto> getCommitteeBills(LocalDate proposeDate) {
        var committeeBillDtoList = billTimelineService.findCommitteesWithMultipleBillIds(proposeDate);
        return committeeBillDtoList.stream()
                .map(committeeBillDto -> {
                    var bills = billService.findBillsWithPartiesByIds(committeeBillDto.getBillIds());

                    var billOutlineDtoList = bills.stream()
                            .map(BillOutlineDto::from)
                            .toList();
                    return CommitteeAuditDto.of(committeeBillDto.getCommitteeName(), billOutlineDtoList);
                })
                .toList();

    }

    public List<SearchKeywordResponse> getRecentSearchWords() {
        var userId = AuthenticationUtil.getUserId()
                .orElseThrow(UserException.UserNotFoundException::new);
        return searchKeywordService.getRecentSearchWords(userId);
    }

    public void makeSearchKeywordAndGetRecentSearchWords(String keyword) {
        var userId = AuthenticationUtil.getUserId()
                        .orElseThrow(UserException.UserNotFoundException::new);

        var user = userService.findById(userId);
        searchKeywordService.makeSearchKeyword(user, keyword);
    }

    public void removeRecentSearchWord(String keyword) {
        var userId = AuthenticationUtil.getUserId()
                        .orElseThrow(UserException.UserNotFoundException::new);
        searchKeywordService.removeRecentSearchWord(userId, keyword);
    }



    public List<ParliamentaryPartyResponse> getParliamentaryParty() {
        return partyService.getParliamentaryParty();
    }



}
