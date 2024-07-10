package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.request.BillDfRequest;
import com.everyones.lawmaking.common.dto.request.BillResultDfRequest;
import com.everyones.lawmaking.common.dto.request.BillStageDfRequest;
import com.everyones.lawmaking.common.dto.request.LawmakerDfRequest;
import com.everyones.lawmaking.domain.entity.*;
import com.everyones.lawmaking.global.error.BillException;
import com.everyones.lawmaking.global.error.CongressmanException;
import com.everyones.lawmaking.global.error.PartyException;
import com.everyones.lawmaking.repository.*;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
// 데이터 변경에 관한 서비스이므로 readOnly 제거
public class DataService {
    private final BillRepository billRepository;
    private final CongressmanRepository congressmanRepository;
    private final BillProposerRepository billProposerRepository;
    private final RepresentativeProposerRepository representativeProposerRepository;
    private final PartyRepository partyRepository;

    @Transactional
    public void insertBillInfoDf(List<BillDfRequest> billDfRequestList) {

//        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // stream으로 dto 각각을 맞는 부분에 넣어야함.
        // 중간에 로직이 필요하면 로직을 거쳐서 들어가자.
        // 시점 파티 데이터 넣기
        Integer assemblyNumber = billDfRequestList.get(0).getAssemblyNumber();
        log.debug(billDfRequestList.toString());
        billDfRequestList
            .forEach(bi -> {

                //낙관적 락

                //partyName으로 partyId조회해서 리스트 반환하기

                List<Long> partyIdList = new ArrayList<>();
                List<String> partyNameList = bi.getRstProposerPartyNameList();
                for (String name : partyNameList) {
                    Party party = partyRepository.findPartyDetailByName(name)
                            .orElseThrow(() -> new PartyException.PartyNotFound(Map.of("party", name)));
                    partyIdList.add(party.getId());
                }
                    // 미완성 Bill 객체 생성하여 다른 필요한 자식들에게 넣어주기.
                    Bill newBill = Bill.of(bi,partyIdList);
                    billRepository.save(newBill);



                    //이름으로 congressmanId를 찾아서 billProposer 저장하기
                bi.getPublicProposers()
                        .forEach(congressmanName -> {
                            billProposerUpdate(newBill, assemblyNumber, congressmanName);
                                }
                        );
                    //대표발의자 이름 검색해서 RP 찾기
                    var representativeProposerName = bi.getRstProposerNameList();
                representativeProposerName.forEach((rpName) -> {
                    updateRepresentativeProposer(newBill, rpName, assemblyNumber);
                        }
                );

                }
            );

    }

    @Transactional
    public List<Long> updateBillStageDf(List<BillStageDfRequest> billStageDfRequestList) {
        List<Long> result = new ArrayList<>();
        billStageDfRequestList.forEach(
                bsr -> {
                    var billNumber = bsr.getBillNumber();
                    var foundBill = billRepository.findBillByNumber(billNumber)
                            .orElse(null);
                    if (foundBill == null) {
                        result.add(billNumber);
                    } else {
                        foundBill.setStage(bsr.getStage());
                    }
                }
        );
        return result;
    }

    @Transactional
    public void updateBillResultDf(List<BillResultDfRequest> billResultDfRequestList) {
        billResultDfRequestList.forEach(
                // bill number로 bill을 찾고 내용 수정하자!
                (bsr) -> {
                    var billNumber = bsr.getBillNumber();
                    var foundBill = billRepository.findBillByNumber(billNumber)
                            .orElseThrow(() -> new BillException.BillNotFound(Map.of("bill", String.valueOf(billNumber))));
                    foundBill.setBillResult(bsr.getBillProposeResult());
                }
        );
    }

    public void updateLawmakerDf(List<LawmakerDfRequest> lawmakerDfRequestList) {

        lawmakerDfRequestList.forEach(
                // bill number로 bill을 찾고 내용 수정하자!
                this::updateLawmaker
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void billProposerUpdate(Bill newBill, Integer assemblyNumber, String congressmanName){
        boolean publicProposersUpdated = false;
        while (!publicProposersUpdated) {
            try{

                var congressman = congressmanRepository.findCongressmanByCongressmanName(congressmanName,assemblyNumber)
                        .orElseThrow(() -> new CongressmanException.CongressmanNotFound(Map.of("congressman", congressmanName)));
                var newBillProposer = BillProposer.builder()
                        .bill(newBill)
                        .congressman(congressman)
                        .build();
                billProposerRepository.save(newBillProposer);
                congressman.setPublicCount(congressman.getPublicCount() + 1);
                publicProposersUpdated = true;
            }
            //트랜잭션관리를 위해서 예외처리
            catch(OptimisticLockException e){
                log.warn("pP update lock conflict", e);

            }

        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateRepresentativeProposer(Bill newBill, String rpName,Integer assemblyNumber){
        boolean representativeUpdated = false;
        while (!representativeUpdated) {
            try {
                var congressmanForRep = congressmanRepository.findCongressmanByCongressmanName(rpName,assemblyNumber)
                        .orElseThrow(() -> new CongressmanException.CongressmanNotFound(Map.of("congressman", rpName)));

                var repProposer = RepresentativeProposer.builder()
                        .bill(newBill)
                        .congressman(congressmanForRep)
                        .build();
                congressmanForRep.setRepresentCount(congressmanForRep.getRepresentCount() + 1);
                //RP 저장
                representativeProposerRepository.save(repProposer);
                representativeUpdated = true;
            }

            //트랜잭션관리를 위해서 예외처리
            catch(OptimisticLockException e){
                log.warn("rp update lock conflict", e);

            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateLawmaker(LawmakerDfRequest ldr){
        boolean updated = false;
        while (!updated) {
            try {
                var congressman = congressmanRepository.findCongressmanById(ldr.getCongressmanId()).orElse(null);
                String partyName = ldr.getPartyName();
                var party = partyRepository.findPartyDetailByName(partyName).orElse(null);

                if (party == null) {
                    party = Party.create(partyName, ldr.getDistrict());
                }

                if (congressman != null) {
                    congressman.update(ldr, party);
                } else {
                    congressman = Congressman.of(ldr, party);
                    party.update(ldr.getDistrict());
                }

                // 정당 정보 업데이트

                // 트랜잭션 커밋 시점에 Optimistic Lock 검증
                partyRepository.save(party);
                congressmanRepository.save(congressman);

                updated = true;
            //트랜잭션관리를 위해서 예외처리
            } catch (OptimisticLockException e) {
                // 충돌 발생 시 재시도
                log.warn("Optimistic lock conflict", e);
            }
        }
    }

}
