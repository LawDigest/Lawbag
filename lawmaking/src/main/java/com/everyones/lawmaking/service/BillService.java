package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.common.dto.BillInfoDto;
import com.everyones.lawmaking.common.dto.PublicProposerDto;
import com.everyones.lawmaking.common.dto.RepresentativeProposerDto;
import com.everyones.lawmaking.common.dto.response.BillListResponse;
import com.everyones.lawmaking.common.dto.response.PaginationResponse;
import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        var billIdList = billSlice.stream()
                .map(Bill::getId)
                .toList();
        var billList = billRepository.findBillInfoByIdList(billIdList);
        var billInfoList = billList.stream()
                .map(this::getBillInfoFromBill)
                .toList();
        var pagination = PaginationResponse.fromSlice(billSlice);
        return BillListResponse.builder()
                .paginationResponse(pagination)
                .billList(billInfoList)
                .build();

    }

    public BillListResponse getBillsByStage(Pageable pageable, String stage) {
        var billSlice = billRepository.findDefaultBillsByStage(pageable, stage);

        var billIdList = billSlice.stream()
                .map(Bill::getId)
                .toList();
        var billList = billRepository.findBillInfoByIdList(billIdList);
        var billInfoList = billList.stream()
                .map(this::getBillInfoFromBill)
                .toList();
        var pagination = PaginationResponse.fromSlice(billSlice);

        return BillListResponse.builder()
                .paginationResponse(pagination)
                .billList(billInfoList)
                .build();
    }

    public BillDto getBillWithDetail(String billId) {
        var bill = billRepository.findBillInfoById(billId)
                .orElseThrow(() -> new CustomException(ResponseCode.INTERNAL_SERVER_ERROR));

        return getBillInfoFromBill(bill);
    }



    public BillListResponse getBillInfoFromRepresentativeProposer(String congressmanId, Pageable pageable) {
        var billSlice = billRepository.findByRepresentativeProposer(congressmanId, pageable);

        if (!billSlice.hasContent()) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        var pagination = PaginationResponse.fromSlice(billSlice);

        var billIdList = billSlice
                .stream()
                .map(Bill::getId)
                .toList();

        var billList = billRepository.findBillInfoByIdList(billIdList);
        var billInfoList = billList.stream()
                .map(this::getBillInfoFromBill)
                .toList();
        return BillListResponse.builder()
                .paginationResponse(pagination)
                .billList(billInfoList)
                .build();
    }

    public BillListResponse getBillInfoFromPublicProposer(String congressmanId, Pageable pageable) {
        var billSlice = billRepository.findBillByPublicProposer(congressmanId, pageable);

        if (billSlice.isEmpty()) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        var billIdList = billSlice.stream()
                .map(Bill::getId)
                .toList();


        var billList = billRepository.findBillInfoByIdList(billIdList);

        var pagination = PaginationResponse.fromSlice(billSlice);

        var billInfoList =  billList.stream()
                .map(this::getBillInfoFromBill)
                .toList();
        return BillListResponse.builder()
                .paginationResponse(pagination)
                .billList(billInfoList)
                .build();
    }

    public BillListResponse getRepresentativeBillsByParty(Pageable pageable, long partyId) {
        var billSlice = billRepository.findRepresentativeBillsByParty(pageable, partyId);

        if (!billSlice.hasContent()) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        var pagination = PaginationResponse.fromSlice(billSlice);

        var billIdList = billSlice
                .stream()
                .map(Bill::getId)
                .toList();
        var billList = billRepository.findBillInfoByIdList(billIdList);
        var billInfoList = billList.stream()
                .map(this::getBillInfoFromBill)
                .toList();

        return BillListResponse.builder()
                .paginationResponse(pagination)
                .billList(billInfoList)
                .build();
    }

    public BillListResponse getPublicBillsByParty(Pageable pageable, long partyId) {
        var billSlice = billRepository.findPublicBillsByParty(pageable, partyId);

        if (!billSlice.hasContent()) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        var pagination = PaginationResponse.fromSlice(billSlice);

        var billIdList = billSlice
                .stream()
                .map(Bill::getId)
                .toList();
        var billList = billRepository.findBillInfoByIdList(billIdList);
        var billInfoList = billList.stream()
                .map(this::getBillInfoFromBill)
                .toList();

        return BillListResponse.builder()
                .paginationResponse(pagination)
                .billList(billInfoList)
                .build();
    }


    private BillDto getBillInfoFromBill(Bill bill) {

        // Bill Entity To DTO
        var billInfoDto = BillInfoDto.fromBill(bill);

        // Representative Entity
        var representativeProposer = bill.getRepresentativeProposer();

        // PublicProposer Entity
        var publicProposers = bill.getPublicProposer();

        var publicProposerDtoList = publicProposers.stream()
                .map(PublicProposerDto::fromPublicProposer)
                .toList();

        var representativeProposerDto = RepresentativeProposerDto.fromRepresentativeProposer(representativeProposer);

        return BillDto.builder()
                .billInfoDto(billInfoDto)
                .representativeProposerDto(representativeProposerDto)
                .publicProposerDtoList(publicProposerDtoList)
                .build();
    }



}

