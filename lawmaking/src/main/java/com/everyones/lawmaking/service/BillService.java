package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.*;
import com.everyones.lawmaking.common.dto.response.*;
import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.global.util.AuthenticationUtil;
import com.everyones.lawmaking.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class BillService {
    private final BillRepository billRepository;

    public Bill getBillEntityById(String billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new CustomException(ResponseCode.INTERNAL_SERVER_ERROR));
    }

    public BillListResponse getBillsByDefault(Pageable pageable) {
        // 메인피드에서 Bill 정보 페이지네이션으로 가져오기
        var billSlice = billRepository.findDefaultBillsByPage(pageable);
        return getBillListResponse(billSlice);

    }

    public BillListResponse getBillsByStage(Pageable pageable, String stage) {
        var billSlice = billRepository.findDefaultBillsByStage(pageable, stage);
        return getBillListResponse(billSlice);
    }

    public BillDto getBillWithDetail(String billId) {
        var bill = billRepository.findBillInfoById(billId)
                .orElseThrow(() -> new CustomException(ResponseCode.INTERNAL_SERVER_ERROR));

        var billDetailResponse = getBillDetailInfoFrom(bill);
        var similarBills = billRepository.findSimilarBills(bill.getBillName())
                .stream()
                .map(SimilarBill::from)
                .toList();
        billDetailResponse.setSimilarBills(similarBills);
        return billDetailResponse;
    }


    // TODO: 조회수 컬럼 접근에 대한 동시성 문제 해결 + 성능 이슈 업데이트 해야함.
    @Transactional
    public BillViewCountResponse updateViewCount(String billId) {
        var bill = billRepository.findById(billId)
                .orElseThrow(() -> new CustomException(ResponseCode.INVALID_QUERY_PARAMETER));
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

        var pagination = PaginationResponse.fromSlice(billSlice);

        var billIdList = billSlice
                .stream()
                .map(Bill::getId)
                .toList();
        var userId = AuthenticationUtil.getUserId();
        List<Bill> billList = billRepository.findBillInfoByIdList(billIdList);

        var billInfoList = billList.stream()
                .map(this::getBillInfoFrom)
                .toList();

        return BillListResponse.builder()
                .paginationResponse(pagination)
                .billList(billInfoList)
                .build();
    }

    @Transactional
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
        var representativeProposer = bill.getRepresentativeProposer();

        // PublicProposer Entity
        var publicProposers = bill.getPublicProposer();

        var publicProposerDtoList = publicProposers.stream()
                .map(PublicProposerDto::fromPublicProposer)
                .toList();

        var representativeProposerDto = RepresentativeProposerDto.from(representativeProposer);

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
                .map(PublicProposerDto::fromPublicProposer)
                .toList();

        var representativeProposerDto = RepresentativeProposerDto.from(representativeProposer);

        return new BillDetailResponse(billInfoDto, representativeProposerDto, publicProposerDtoList, false);
    }



    // 법원 검색
    public SearchDataResponse searchBill(String searchWord,Pageable pageable) {
        var billSlice = billRepository.findBillByKeyword(pageable,searchWord);

        var pagination = PaginationResponse.fromSlice(billSlice);


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

