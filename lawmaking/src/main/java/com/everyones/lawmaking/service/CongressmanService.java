package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.CongressmanDto;
import com.everyones.lawmaking.common.dto.BillDto;
import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.domain.entity.Congressman;
import com.everyones.lawmaking.repository.CongressmanRepository;
import com.everyones.lawmaking.repository.BillRepository;
import com.everyones.lawmaking.repository.BillProposerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CongressmanService {
    private final CongressmanRepository congressmanRepository;
    private final BillRepository billRepository;

    public CongressmanDto getCongressmanDetails(String id) {
        Congressman congressman = congressmanRepository.findCongressmanById(id);
        List<Bill> bills = billRepository.findAllBillsByCongressmanId(id); // 수정됨: List<Bill> 반환
        List<BillDto> billDtos = bills.stream()
                .map(this::convertToBillDto)
                .collect(Collectors.toList());
        return convertToCongressmanDto(congressman, billDtos);
    }
    private BillDto convertToBillDto(Bill bill) {
        return BillDto.builder()
                .billId(bill.getId())
                .billName(bill.getBillName()) // 여기를 수정함
                .representProposer(bill.getRepresentProposer().getName())
                .summary(bill.getSummary()) // 여기를 수정함
                .proposeDate(bill.getProposeDate()) // 여기를 수정함
//                .view(0) // 기본값 설정
//                .like(0) // 기본값 설정
                .build();
    }

    private CongressmanDto convertToCongressmanDto(Congressman congressman, List<BillDto> bills) {
        return CongressmanDto.builder()
                .congressmanId(congressman.getId())
                .name(congressman.getName())
                .partyName(congressman.getParty().getName())
                .electSort(congressman.getElectSort())
                .district(congressman.getDistrict())
                .commits(congressman.getCommits())
                .elected(congressman.getElected())
                .homepage(congressman.getHomepage())
                .representCount(congressman.getRepresentCount())
                .publicCount(congressman.getPublicCount())
                .bills(bills)
                .build();
    }
}