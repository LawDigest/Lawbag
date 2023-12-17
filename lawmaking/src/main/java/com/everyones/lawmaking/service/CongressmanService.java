package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.BaseResponse;
import com.everyones.lawmaking.common.dto.CongressDetailBillDto;
import com.everyones.lawmaking.common.dto.CongressmanDto;
import com.everyones.lawmaking.common.dto.response.PaginationResponse;
import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.domain.entity.BillProposer;
import com.everyones.lawmaking.domain.entity.Congressman;
import com.everyones.lawmaking.domain.entity.Party;
import com.everyones.lawmaking.repository.BillProposerRepository;
import com.everyones.lawmaking.repository.BillRepository;
import com.everyones.lawmaking.repository.CongressmanRepository;
import com.everyones.lawmaking.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CongressmanService {
    private final CongressmanRepository congressmanRepository;
    private final BillProposerRepository billProposerRepository;
    private final BillRepository billRepository;
    private final PartyRepository partyRepository;

    public Map<String, Object> getCongressmanDetails(String congressmanId, Pageable pageable) {
        Congressman congressman = congressmanRepository.findByIdWithParty(congressmanId)
                .orElseThrow(() -> new RuntimeException("Congressman not found"));

        CongressmanDto congressmanDto = buildCongressmanDto(congressman);
        Page<BillProposer> representativeProposersPage = billProposerRepository.findRepresentativeByCongressmanId(congressmanId, pageable);

        List<String> billIds = representativeProposersPage.getContent().stream()
                .map(bp -> bp.getBill().getId())
                .collect(Collectors.toList());

        List<CongressDetailBillDto> detailedBills = buildDetailedBillDtos(billIds);
        // PaginationResponse 객체 생성
        PaginationResponse paginationResponse = new PaginationResponse(representativeProposersPage.isLast(), representativeProposersPage.getNumber());

        // API 응답 데이터 구조 조립
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("pagination_response", paginationResponse);
        responseData.put("congressman", congressmanDto);
        responseData.put("bills", detailedBills);

        // BaseResponse 포함하여 반환
        return BaseResponse.generateSuccessResponse(responseData);
    }

    private List<CongressDetailBillDto> buildDetailedBillDtos(List<String> billIds) {
        List<CongressDetailBillDto> detailedBills = new ArrayList<>();
        for (String billId : billIds) {
            Bill bill = billRepository.findById(billId)
                    .orElseThrow(() -> new RuntimeException("Bill not found"));
            List<BillProposer> proposers = billProposerRepository.findByBillId(billId);
            BillProposer representativeProposer = getRepresentativeProposer(proposers);

            detailedBills.add(buildDetailedBillDto(bill, representativeProposer, proposers));
        }
        return detailedBills;
    }


    private CongressDetailBillDto buildDetailedpulbicBillDto(Bill bill, BillProposer representativeProposer, List<BillProposer> proposers) {
        // Extract party names and IDs
        List<String> partyNames = proposers.stream()
                .map(proposer -> proposer.getCongressman().getParty().getName())
                .distinct()
                .collect(Collectors.toList());

        List<Long> partyIds = proposers.stream()
                .map(proposer -> proposer.getCongressman().getParty().getId())
                .distinct()
                .collect(Collectors.toList());

        // Fetch party image URLs
        List<String> partyImageUrls = partyRepository.findAllByIds(partyIds).stream()
                .map(Party::getPartyImageUrl)
                .collect(Collectors.toList());

        // Initialize representative proposer details
        String representProposerName = null;
        String representProposerId = null;
        String representProposerParty = null;
        Long representProposerPartyId = null;
        String representProposerImgUrl = null; // Initialize as null

        // Assign details if representative proposer is not null
        if (representativeProposer != null) {
            representProposerName = representativeProposer.getCongressman().getName();
            representProposerId = representativeProposer.getCongressman().getId();
            representProposerParty = representativeProposer.getCongressman().getParty().getName();
            representProposerPartyId = representativeProposer.getCongressman().getParty().getId();
            representProposerImgUrl = representativeProposer.getCongressman().getCongressmanImageUrl(); // Fetch the URL here
        }

        // Construct the CongressDetailBillDto object
        return CongressDetailBillDto.builder()
                .billId(bill.getId())
                .billName(bill.getBillName())
                .proposeDate(bill.getProposeDate())
                .proposers(bill.getProposers())
                .summary(bill.getSummary())
                .gptSummary(bill.getGptSummary())
                .representProposer(representativeProposer.getCongressman().getName())
                .representProposerId(representativeProposer.getCongressman().getId())
                .representProposerImgUrl(representProposerImgUrl)
                .representProposerParty(representativeProposer.getCongressman().getParty().getName())
                .representProposerPartyId(representativeProposer.getCongressman().getParty().getId())
                .representProposerPartyImgUrl(representativeProposer.getCongressman().getParty().getPartyImageUrl())
                .partyList(partyNames)
                .partyIdList(partyIds)
                .partyImageUrls(partyImageUrls)
                .build(); // Ensure all the necessary fields are set
    }

    private CongressmanDto buildCongressmanDto(Congressman congressman) {
        return CongressmanDto.builder()
                .id(congressman.getId())
                .name(congressman.getName())
                .partyName(congressman.getParty().getName())
                .partyImageUrl(congressman.getParty().getPartyImageUrl())
                .electSort(congressman.getElectSort())
                .district(congressman.getDistrict())
                .commits(congressman.getCommits())
                .elected(congressman.getElected())
                .homepage(congressman.getHomepage())
                .representCount(congressman.getRepresentCount())
                .publicCount(congressman.getPublicCount())
                .congressmanImageUrl(congressman.getCongressmanImageUrl()) // Set the dynamically constructed image URL
                .build();
    }

    private BillProposer getRepresentativeProposer(List<BillProposer> proposers) {
        return proposers.stream()
                .filter(BillProposer::isRepresent)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Representative proposer not found"));
    }
////////////////////////////////////////////////////////////////////////////////////////////
//public Map<String, Object> getCongressmanCoSponsorshipDetails(String congressmanId, Pageable pageable) {


//
//    // 대표 발의자로 있는 법안 ID 조회
//    List<String> representativeBillIds = billProposerRepository.findRepresentativeBillIdsByCongressmanId(congressmanId);
//
//    Page<BillProposer> coSponsorshipProposersPage = billProposerRepository.findCoSponsorshipsByCongressmanId(congressmanId, pageable);
//
//    List<String> billIds = coSponsorshipProposersPage.getContent().stream()
//            .map(bp -> bp.getBill().getId())
//            .collect(Collectors.toList());
//
//    List<CongressDetailBillDto> detailedBills = buildDetailedBillDtos(billIds, representativeBillIds);
    public Map<String, Object> getCongressmanCoSponsorshipDetails(String congressmanId, Pageable pageable) {
        Congressman congressman = congressmanRepository.findByIdWithParty(congressmanId)
                .orElseThrow(() -> new RuntimeException("Congressman not found"));
        CongressmanDto congressmanDto = buildCongressmanDto(congressman);

        //    // 대표 발의자로 있는 법안 ID 조회
//        List<String> representativeBillIds = billProposerRepository.findRepresentativeBillIdsByCongressmanId(congressmanId);
        Page<BillProposer> coSponsorshipProposersPage = billProposerRepository.findCoSponsorshipsByCongressmanId(congressmanId, pageable);
        // Fetch co-sponsorship bills
        // Fetch co-sponsorship bills
        List<String> billIds = coSponsorshipProposersPage.getContent().stream()
                .map(bp -> bp.getBill().getId())
                .collect(Collectors.toList());

        List<CongressDetailBillDto> detailedBills = new ArrayList<>();
        for (String billId : billIds) {
            Bill bill = billRepository.findById(billId)
                    .orElseThrow(() -> new RuntimeException("Bill not found"));
            List<BillProposer> proposers = billProposerRepository.findByBillId(billId);

            // Find the representative proposer for each bill
            BillProposer representativeProposer = proposers.stream()
                    .filter(BillProposer::isRepresent)
                    .findFirst()
                    .orElse(null);

            detailedBills.add(buildDetailedpulbicBillDto(bill, representativeProposer, proposers));
        }
            // 기존 로직으로 CongressmanDto와 bills 목록을 가져옴

            // PaginationResponse 객체 생성
        PaginationResponse paginationResponse = new PaginationResponse(coSponsorshipProposersPage.isLast(), coSponsorshipProposersPage.getNumber());

        // API 응답 데이터 구조 조립
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("pagination_response", paginationResponse);
        responseData.put("congressman", congressmanDto);
        responseData.put("bills", detailedBills);

        // BaseResponse 포함하여 반환
        return BaseResponse.generateSuccessResponse(responseData);
    }

//    private List<CongressDetailBillDto> buildDetailedPublicBillDtos(List<String> billIds, List<String> representativeBillIds) {
//        List<CongressDetailBillDto> detailedBills = new ArrayList<>();
//        for (String billId : billIds) {
//            Bill bill = billRepository.findById(billId)
//                    .orElseThrow(() -> new RuntimeException("Bill not found"));
//            List<BillProposer> proposers = billProposerRepository.findByBillId(billId);
//
//            // 대표 제안자 찾기
//            BillProposer representativeProposer = proposers.stream()
//                    .filter(bp -> representativeBillIds.contains(bp.getBill().getId()) && bp.isRepresent())
//                    .findFirst()
//                    .orElse(null);
//
//            detailedBills.add(buildDetailedpulbicBillDto(bill, representativeProposer, proposers));
//        }
//        return detailedBills;
//    }


    // CongressmanService 클래스 내의 메소드
    private CongressDetailBillDto buildDetailedBillDto(Bill bill, BillProposer representativeProposer, List<BillProposer> proposers) {
        // Extract party names and IDs
        List<String> partyNames = proposers.stream()
                .map(proposer -> proposer.getCongressman().getParty().getName())
                .distinct()
                .collect(Collectors.toList());

        List<Long> partyIds = proposers.stream()
                .map(proposer -> proposer.getCongressman().getParty().getId())
                .distinct()
                .collect(Collectors.toList());

        // Fetch party image URLs
        List<String> partyImageUrls = partyRepository.findAllByIds(partyIds).stream()
                .map(Party::getPartyImageUrl)
                .collect(Collectors.toList());

        // Initialize representative proposer details
        String representProposerName = null;
        String representProposerId = null;
        String representProposerParty = null;
        Long representProposerPartyId = null;
        String representProposerImgUrl = null;
        String representProposerPartyImgUrl = null;

        // Assign details if representative proposer is not null
        if (representativeProposer != null) {
            representProposerName = representativeProposer.getCongressman().getName();
            representProposerId = representativeProposer.getCongressman().getId();
            representProposerParty = representativeProposer.getCongressman().getParty().getName();
            representProposerPartyId = representativeProposer.getCongressman().getParty().getId();
            representProposerImgUrl = representativeProposer.getCongressman().getCongressmanImageUrl();
            representProposerPartyImgUrl = representativeProposer.getCongressman().getParty().getPartyImageUrl();

            // Check and add the party image URL if it's not already in the list
            if (!partyImageUrls.contains(representProposerPartyImgUrl)) {
                partyImageUrls.add(0, representProposerPartyImgUrl);
            }
        }

        // Build and return the DTO
        return CongressDetailBillDto.builder()
                .billId(bill.getId())
                .billName(bill.getBillName())
                .proposeDate(bill.getProposeDate())
                .proposers(bill.getProposers())
                .summary(bill.getSummary())
                .gptSummary(bill.getGptSummary())
                .partyList(partyNames)
                .partyIdList(partyIds)
                .representProposer(representProposerName)
                .representProposerId(representProposerId)
                .representProposerParty(representProposerParty)
                .representProposerPartyId(representProposerPartyId)
                .representProposerImgUrl(representProposerImgUrl)
                .representProposerPartyImgUrl(representProposerPartyImgUrl)
                .partyImageUrls(partyImageUrls)
                .build();
    }

}

