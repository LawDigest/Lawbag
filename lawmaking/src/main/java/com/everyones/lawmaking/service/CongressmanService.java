package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.CongressDetailBillDto;
import com.everyones.lawmaking.common.dto.CongressmanDto;
import com.everyones.lawmaking.common.dto.response.CongressmanDetailResponse;
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
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CongressmanService {
    private final CongressmanRepository congressmanRepository;
    private final BillProposerRepository billProposerRepository;
    private final BillRepository billRepository;

    public CongressmanDetailResponse getCongressmanDetails(String congressmanId, Pageable pageable) {
        Congressman congressman = congressmanRepository.findByIdWithParty(congressmanId)
                .orElseThrow(() -> new RuntimeException("Congressman not found"));

        CongressmanDto congressmanDto = buildCongressmanDto(congressman);
        Page<BillProposer> representativeProposersPage = billProposerRepository.findRepresentativeByCongressmanId(congressmanId, pageable);

        List<String> billIds = representativeProposersPage.getContent().stream()
                .map(bp -> bp.getBill().getId())
                .collect(Collectors.toList());

        List<CongressDetailBillDto> detailedBills = buildDetailedBillDtos(billIds);

        congressmanDto.setRepresentativeBills(detailedBills);

        return new CongressmanDetailResponse(
                congressmanDto,
                representativeProposersPage.isLast(),
                representativeProposersPage.getNumber()
        );
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


    private CongressDetailBillDto buildDetailedBillDto(Bill bill, BillProposer representativeProposer, List<BillProposer> proposers) {
        // Extract party names and IDs for all proposers
        List<String> partyNames = proposers.stream()
                .map(proposer -> proposer.getCongressman().getParty().getName())
                .distinct()
                .collect(Collectors.toList());
        List<Long> partyIds = proposers.stream()
                .map(proposer -> proposer.getCongressman().getParty().getId())
                .distinct()
                .collect(Collectors.toList());

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
                .representProposerParty(representativeProposer.getCongressman().getParty().getName())
                .representProposerPartyId(representativeProposer.getCongressman().getParty().getId())
                .representProposerImgUrl(representativeProposer.getCongressman().getParty().getPartyImageUrl())
                .partyList(partyNames)
                .partyIdList(partyIds)
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
                .build();
    }

    private BillProposer getRepresentativeProposer(List<BillProposer> proposers) {
        return proposers.stream()
                .filter(BillProposer::isRepresent)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Representative proposer not found"));
    }
}

