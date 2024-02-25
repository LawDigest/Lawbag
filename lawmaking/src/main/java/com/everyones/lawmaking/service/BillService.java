package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.*;
import com.everyones.lawmaking.common.dto.response.BillDetailDto;
import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.common.dto.response.PaginationResponse;
import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.global.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.repository.BillProposerRepository;
import com.everyones.lawmaking.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;



@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class BillService {
    private final BillRepository billRepository;
    private final BillProposerRepository billProposerRepository;

    public List<BillDto> getBillsByDefault(Pageable pageable) {
        // 메인피드에서 Bill 정보 페이지네이션으로 가져오기
        var billSlice = billRepository.findDefaultBillsByPage(pageable);
        var billIdList = billSlice.stream()
                .map(Bill::getId)
                .toList();
        var billList = billRepository.findBillInfoByIdList(billIdList);
        var billInfoList = billList.stream()
                .map(bill -> getBillInfoFromBill(bill))
                .toList();
        var pagination = PaginationResponse.fromSlice(billSlice);

        return billInfoList;

    }

    public List<BillDto> getBillsByStage(Pageable pageable, String stage) {
        var billSlice = billRepository.findDefaultBillsByStage(pageable, stage);
        var billIdList = billSlice.stream()
                .map(Bill::getId)
                .toList();
        var billList = billRepository.findBillInfoByIdList(billIdList);
        var billInfoList = billList.stream()
                .map(bill -> getBillInfoFromBill(bill))
                .toList();
        var pagination = PaginationResponse.fromSlice(billSlice);

        return billInfoList;
    }

    public BillDto getBillWithDetail(String billId) {
        var bill = billRepository.findBillInfoById(billId)
                .orElseThrow(() -> new CustomException(ResponseCode.INTERNAL_SERVER_ERROR));
        var billDto = getBillInfoFromBill(bill);

        return billDto;
    }



    public List<BillDto> getBillInfoFromRepresentativeProposer(String congressmanId, Pageable pageable) {
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

        return billList.stream()
                .map(bill -> getBillInfoFromBill(bill))
                .toList();
    }

    public List<BillDto> getBillInfoFromPublicProposer(String congressmanId, Pageable pageable) {
        var billIdList = billRepository.findBillByPublicProposer(congressmanId, pageable)
                .stream()
                .map(Bill::getId)
                .collect(Collectors.toList());

        if (billIdList.isEmpty()) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        var billList = billRepository.findBillInfoByIdList(billIdList);

        return billList.stream()
                .map(bill -> getBillInfoFromBill(bill))
                .toList();
    }


    private BillDto getBillInfoFromBill(Bill bill) {
        var billInfoDto = BillInfoDto.fromBill(bill);
        var representativeProposer = bill.getRepresentativeProposer();
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

