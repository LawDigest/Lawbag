package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.*;
import com.everyones.lawmaking.common.dto.response.BillDetailResponse;
import com.everyones.lawmaking.common.dto.response.BillListResponse;
import com.everyones.lawmaking.common.dto.response.BillViewCountResponse;
import com.everyones.lawmaking.common.dto.response.PaginationResponse;
import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.global.constant.BillOrderType;
import com.everyones.lawmaking.global.error.BillException;
import com.everyones.lawmaking.global.error.UserException;
import com.everyones.lawmaking.global.util.AuthenticationUtil;
import com.everyones.lawmaking.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        return getBillListResponse(billSlice, BillOrderType.BASIC);

    }

    // 단계 추가
    public BillListResponse findByPage(Pageable pageable, String stage) {
        var billSlice = billRepository.findByPage(pageable, stage);
        return getBillListResponse(billSlice, BillOrderType.BASIC);
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
        return getBillListResponse(billSlice, BillOrderType.BILL_LIKE_ORDER);
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
        return getBillListResponse(billSlice, BillOrderType.BASIC);

    }

    public BillListResponse getBillInfoFromPublicProposer(String congressmanId, Pageable pageable) {
        var billSlice = billRepository.findBillByPublicProposer(congressmanId, pageable);
        return getBillListResponse(billSlice, BillOrderType.BASIC);

    }

    public BillListResponse getRepresentativeBillsByParty(Pageable pageable, long partyId) {
        var billSlice = billRepository.findRepresentativeBillsByParty(pageable, partyId);
        return getBillListResponse(billSlice, BillOrderType.BASIC);

    }

    public BillListResponse getPublicBillsByParty(Pageable pageable, long partyId) {
        var billSlice = billRepository.findPublicBillsByParty(pageable, partyId);
        return getBillListResponse(billSlice, BillOrderType.BASIC);

    }

    public BillListResponse findByUserAndCongressmanLike(Pageable pageable) {
        var userId = AuthenticationUtil.getUserId();
        if (userId.isEmpty()) {
            throw new UserException.UserNotFoundException();
        }
        var billList = billRepository.findByUserAndCongressmanLike(pageable, userId.get());
        return getBillListResponse(billList, BillOrderType.BASIC);
    }

    public BillListResponse getBillListResponse(Slice<Bill> billSlice, BillOrderType billOrderType) {

        var pagination = PaginationResponse.from(billSlice);

        var billIdList = billSlice
                .stream()
                .map(Bill::getId)
                .toList();

        var billList = getSortedBillByOrderType(billIdList, billOrderType);

        var billInfoList = billList.stream()
                .map(this::getBillInfoFrom)
                .toList();

        return BillListResponse.builder()
                .paginationResponse(pagination)
                .billList(billInfoList)
                .build();
    }

    public List<Bill> getSortedBillByOrderType(List<String> billIdList, BillOrderType billOrderType) {
        return billRepository.findBillInfoByIdList(billIdList);
    }



    public List<BillDto> getBillListResponse(List<String> billIdList) {
        var billList = billRepository.findBillInfoByIdList(billIdList);

        return billList.stream()
                .map(this::getBillInfoFrom)
                .toList();
    }

    // 메인피드 등 법안들의 리스트를 반환할 때 사용
    private BillDto getBillInfoFrom(Bill bill) {

        // Bill Entity To DTO
        var billInfoDto = BillInfoDto.from(bill);

        // Representative Entity
        var representativeProposerDto = bill.getRepresentativeProposer().stream()
                .map(RepresentativeProposerDto::from)
                .toList();
        // PublicProposer Entity
        var publicProposerDtoList = bill.getPublicProposer().stream()
                .map(PublicProposerDto::from)
                .toList();

        return BillDto.builder()
                .billInfoDto(billInfoDto)
                .representativeProposerDtoList(representativeProposerDto)
                .publicProposerDtoList(publicProposerDtoList)
                .isBookMark(false)
                .build();
    }
    



    private BillDetailResponse getBillDetailInfoFrom(Bill bill) {

        // Bill Entity To DTO
        var billInfoDto = BillDetailInfo.from(bill);

        // Representative Entity
        var representativeProposers = bill.getRepresentativeProposer();

        // PublicProposer Entity
        var publicProposers = bill.getPublicProposer();

        var publicProposerDtoList = publicProposers.stream()
                .map(PublicProposerDto::from)
                .toList();


        var representativeProposerDtoList = representativeProposers.stream()
                .map(RepresentativeProposerDto::from)
                .toList();

        return new BillDetailResponse(billInfoDto, representativeProposerDtoList, publicProposerDtoList, false);
    }



    // 법원 검색
    public BillListResponse searchBill(String searchWord,Pageable pageable) {
        var billSlice = billRepository.findBillByKeyword(pageable,searchWord);

        var pagination = PaginationResponse.from(billSlice);


        var billIdList = billSlice.toList();

        var billList = billRepository.findBillInfoByIdList(billIdList);
        List<BillDto> billDtoList = billList.stream()
                .map(this::getBillInfoFrom)
                .toList();

        return BillListResponse.builder()
                .paginationResponse(pagination)
                .billList(billDtoList)
                .build();

    }




}

