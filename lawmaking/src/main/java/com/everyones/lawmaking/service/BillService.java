package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.*;
import com.everyones.lawmaking.common.dto.response.*;
import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.global.error.BillException;
import com.everyones.lawmaking.global.util.AuthenticationUtil;
import com.everyones.lawmaking.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class BillService {
    private final BillRepository billRepository;

    private static final String BILL_ID_KEY_STRING = "billId";

    public Bill findById(String billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new BillException.BillNotFound(Map.of(BILL_ID_KEY_STRING, billId)));
    }

    // 메인피드에서 Bill 정보 페이지네이션으로 가져오기
    public BillListResponse findByPage(Pageable pageable) {
        var billSlice = billRepository.findByPage(pageable);
        return getBillListResponse(billSlice);

    }

    // 단계 추가
    public BillListResponse findByPage(Pageable pageable, String stage) {
        var billSlice = billRepository.findByPage(pageable, stage);
        return getBillListResponse(billSlice);
    }

    public BillDetailResponse getBillWithDetail(String billId) {
        var bill = billRepository.findBillInfoById(billId)
                .orElseThrow(() -> new BillException.BillNotFound(Map.of(BILL_ID_KEY_STRING, billId)));

        var billDetailResponse = getBillDetailInfoFrom(bill);
        var similarBills = billRepository.findSimilarBills(bill.getBillName(), bill.getId())
                .stream()
                .map(SimilarBill::from)
                .toList();
        billDetailResponse.setSimilarBills(similarBills);
        return billDetailResponse;
    }

    // 유저가 좋아요한 법안 목록 슬라이스로 가져오기
    public BillListResponse getBookmarkingBills(Pageable pageable, long userId) {
        var billSlice = billRepository.findByUserId(pageable, userId);
        return getBillListResponse(billSlice);
    }

    // TODO: 조회수 컬럼 접근에 대한 동시성 문제 해결 + 성능 이슈 업데이트 해야함.
    @Transactional
    public BillViewCountResponse updateViewCount(String billId) {
        var bill = billRepository.findById(billId)
                .orElseThrow(() -> new BillException.BillNotFound(Map.of(BILL_ID_KEY_STRING, billId)));
        bill.setViewCount(bill.getViewCount()+1);
        var updatedBill = billRepository.save(bill);
        return BillViewCountResponse.from(updatedBill);
    }

    public BillListResponse getBillInfoFromRepresentativeProposer(String congressmanId, Pageable pageable) {
        var billSlice = billRepository.findByRepresentativeProposer(congressmanId, pageable);
        return getBillListResponse(billSlice);

    }

    public BillListResponse getBillInfoFromPublicProposer(String congressmanId, Pageable pageable) {
        var billSlice = billRepository.findBillByPublicProposer(congressmanId, pageable);
        return getBillListResponse(billSlice);

    }

    public BillListResponse getRepresentativeBillsByParty(Pageable pageable, long partyId) {
        var billSlice = billRepository.findRepresentativeBillsByParty(pageable, partyId);
        return getBillListResponse(billSlice);

    }

    public BillListResponse getPublicBillsByParty(Pageable pageable, long partyId) {
        var billSlice = billRepository.findPublicBillsByParty(pageable, partyId);
        return getBillListResponse(billSlice);

    }

    private BillListResponse getBillListResponse(Slice<Bill> billSlice) {

        var pagination = PaginationResponse.from(billSlice);

        var billIdList = billSlice
                .stream()
                .map(Bill::getId)
                .toList();

        var billList = billRepository.findBillInfoByIdList(billIdList);

        var billInfoList = billList.stream()
                .map(this::getBillInfoFrom)
                .toList();

        var userIdOptional = AuthenticationUtil.getUserId();

        if (userIdOptional.isPresent()) {

            return BillListResponse.builder()
                    .paginationResponse(pagination)
                    .billList(billInfoList)
                    .build();
        }

        return BillListResponse.builder()
                .paginationResponse(pagination)
                .billList(billInfoList)
                .build();
    }


    public void updateBillLikeCount(Bill bill, boolean likeChecked) {
        var likeCount = likeChecked ? bill.getLikeCount() + 1 : bill.getLikeCount() - 1;
        bill.setLikeCount(likeCount);
        billRepository.save(bill);
    }

    // 메인피드 등 법안들의 리스트를 반환할 때 사용
    private BillDto getBillInfoFrom(Bill bill) {

        // Bill Entity To DTO
        var billInfoDto = BillInfoDto.from(bill);

        // Representative Entity
        var representativeProposerDto = RepresentativeProposerDto.from(bill.getRepresentativeProposer());

        // PublicProposer Entity
        var publicProposerDtoList = bill.getPublicProposer().stream()
                .map(PublicProposerDto::from)
                .toList();

        return BillDto.builder()
                .billInfoDto(billInfoDto)
                .representativeProposerDto(representativeProposerDto)
                .publicProposerDtoList(publicProposerDtoList)
                .isBookMark(false)
                .build();
    }

    //
    private BillDetailResponse getBillDetailInfoFrom(Bill bill) {

        // Bill Entity To DTO
        var billInfoDto = BillDetailInfo.from(bill);

        // Representative Entity
        var representativeProposer = bill.getRepresentativeProposer();

        // PublicProposer Entity
        var publicProposers = bill.getPublicProposer();

        var publicProposerDtoList = publicProposers.stream()
                .map(PublicProposerDto::from)
                .toList();

        var representativeProposerDto = RepresentativeProposerDto.from(representativeProposer);

        return new BillDetailResponse(billInfoDto, representativeProposerDto, publicProposerDtoList, false);
    }



    // 법원 검색
    public SearchDataResponse searchBill(String searchWord,Pageable pageable) {
        var billSlice = billRepository.findBillByKeyword(pageable,searchWord);

        var pagination = PaginationResponse.from(billSlice);


        var billIdList = billSlice.toList();

        var billList = billRepository.findBillInfoByIdList(billIdList);

        var searchResponse = billList.stream()
                .map(SearchBillDto::from)
                .toList();

        return SearchDataResponse.builder()
                .searchResponse(searchResponse)
                .paginationResponse(pagination)
                .build();

    }




}

