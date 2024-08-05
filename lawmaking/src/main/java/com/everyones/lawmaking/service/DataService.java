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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

        // stream으로 dto 각각을 맞는 부분에 넣어야함.
        // 중간에 로직이 필요하면 로직을 거쳐서 들어가자.
        // 시점 파티 데이터 넣기
        billDfRequestList
            .forEach(billDfRequest -> {
                var oldBill = billRepository.findBillInfoById(billDfRequest.getBillId())
                        .orElse(null);
                if (oldBill != null){
                    oldBill.update(billDfRequest);
                }
                else {

                    //partyName으로 partyId조회해서 리스트 반환하기
                    List<Long> partyIdList = new ArrayList<>();
                    List<String> partyNameList = billDfRequest.getRstProposerPartyNameList();
                    for (String name : partyNameList) {
                        Party party = partyRepository.findPartyByName(name)
                                .orElseThrow(() -> new PartyException.PartyNotFound(Map.of("party", name)));
                        partyIdList.add(party.getId());
                    }

                    // 미완성 Bill 객체 생성하여 다른 필요한 자식들에게 넣어주기.
                    Bill newBill = Bill.of(billDfRequest, partyIdList);

                    billRepository.save(newBill);


                    //이름으로 congressmanId를 찾아서 billProposer 저장하기
                    //@ToDo 동명이인 필터링을 위해 후에 의원검색에 한글이름, 한자이름, 정당이름 세가지 조건을 추가하기.
                    //현재는 state가 true인 조건만 추가
                    //트랜잭션에서 에러가 발생하면 모든 트랜잭션이 롤백해야함.
                    billDfRequest.getPublicProposers()
                            .forEach(congressmanName -> {
                                var billProposer = congressmanRepository.findLawmakerByName(congressmanName)
                                        .orElseThrow(() -> new CongressmanException.CongressmanNotFound(Map.of("congressman", congressmanName)));
                                        billProposerUpdate(newBill, billProposer);
                                    }
                            );
                    //대표발의자 이름 검색해서 RP 찾기
                    var representativeProposerName = billDfRequest.getRstProposerNameList();
                    representativeProposerName.forEach((rpName) -> {
                        var representativeProposer = congressmanRepository.findLawmakerByName(rpName)
                                .orElseThrow(() -> new CongressmanException.CongressmanNotFound(Map.of("congressman", rpName)));
                        updateRepresentativeProposer(newBill, representativeProposer);
                            }
                    );
                }
                }
            );

    }

    @Transactional
    public List<Long> updateBillStageDf(List<BillStageDfRequest> billStageDfRequestList) {
        List<Long> result = new ArrayList<>();
        billStageDfRequestList.forEach(
                billStageDfRequest -> {
                    var billNumber = billStageDfRequest.getBillNumber();
                    var foundBill = billRepository.findBillByBillNumber(billNumber)
                            .orElse(null);
                    if (foundBill == null) {
                        result.add(billNumber);
                    } else {
                        foundBill.setStage(billStageDfRequest.getStage());
                    }
                }
        );
        return result;
    }

    @Transactional
    public void updateBillResultDf(List<BillResultDfRequest> billResultDfRequestList) {
        billResultDfRequestList.forEach(
                // bill number로 bill을 찾고 내용 수정하자!
                (billResultDfRequest) -> {
                    var billNumber = billResultDfRequest.getBillNumber();
                    var foundBill = billRepository.findBillByBillNumber(billNumber)
                            .orElseThrow(() -> new BillException.BillNotFound(Map.of("bill", String.valueOf(billNumber))));
                    foundBill.setBillResult(billResultDfRequest.getBillProposeResult());
                }
        );
    }

    @Transactional
    public void updateLawmakerDf(List<LawmakerDfRequest> lawmakerDfRequestList) {

        // 삽입 :국회 API 호출 데이터 - (API 교집합 DB의원데이터)
        // state false로 만들기 진행: 디비 의원 데이터 - (API 교집합 DB의원데이터) -> 전부 False로 처리
        // 교집합 처리: API 교집합 DB의원 데이터를 새롭게 업데이트 해주면서 state True로 처리

        // API 데이터 들을 맵으로 만들기
        Map<String,LawmakerDfRequest> lawmakerApiData = new HashMap<>();
        lawmakerDfRequestList.forEach(lawmaker-> {
            lawmakerApiData.put(lawmaker.getCongressmanId(), lawmaker);
        });

        //DB 의원데이터 가져오기
        Set<String> congressmanSet = new HashSet<>(congressmanRepository.findAllCongressmanId());
        Set<String> intersectionWithApiAndDB = new HashSet<>(congressmanSet);

        //API 데이터 집합
        Set<String> lawmakerApi = new HashSet<>(lawmakerApiData.keySet());

        //디비에 존재하는 현재 의원 데이터
        intersectionWithApiAndDB.retainAll(lawmakerApi);

        //삽입해야하는 차집합
        lawmakerApi.removeAll(intersectionWithApiAndDB);
        congressmanSet.removeAll(intersectionWithApiAndDB);

        //삽입 과정 진행
        lawmakerApi.forEach((insertLawmaker)->
                this.insertLawmaker(lawmakerApiData.get(insertLawmaker)));

        //상태 false로 만들기 진행
        congressmanSet.forEach(

                (updateStateFalseCongressman)-> {
                    var congressmanUpdateToFalse = congressmanRepository.findCongressmanById(updateStateFalseCongressman)
                            .orElseThrow(() -> new CongressmanException.CongressmanNotFound(Map.of("congressman", updateStateFalseCongressman)));
                    lawmakerStateToFalse(congressmanUpdateToFalse);
                });

        //수정 과정 진행
        //API로 들어온 의원리스트로 기존 의원 데이터 최신화 시키기
        intersectionWithApiAndDB.forEach((updateStateTrueCongressmanId)->{
            var congressmanUpdateToTrue = congressmanRepository.findCongressmanById(updateStateTrueCongressmanId)
                    .orElseThrow(() -> new CongressmanException.CongressmanNotFound(Map.of("congressman", updateStateTrueCongressmanId)));
            lawmakerStateToTrue(lawmakerApiData.get(updateStateTrueCongressmanId),congressmanUpdateToTrue);
        });

    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void billProposerUpdate(Bill newBill, Congressman billProposer){
        boolean publicProposersUpdated = false;
        while (!publicProposersUpdated) {
            try{
                var newBillProposer = BillProposer.builder()
                        .bill(newBill)
                        .congressman(billProposer)
                        .build();

                billProposerRepository.save(newBillProposer);

                congressmanRepository.save(billProposer);

                publicProposersUpdated = true;

            }
            //트랜잭션관리를 위해서 예외처리
            catch(ObjectOptimisticLockingFailureException e){
                log.warn("pP update lock conflict", e);
            }

        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateRepresentativeProposer(Bill newBill, Congressman representativeProposer){
        boolean representativeUpdated = false;
        while (!representativeUpdated) {
            try {

                var repProposer = RepresentativeProposer.builder()
                        .bill(newBill)
                        .congressman(representativeProposer)
                        .build();

                //RP 저장
                representativeProposerRepository.save(repProposer);
                congressmanRepository.save(representativeProposer);
                representativeUpdated = true;
            }


            //트랜잭션관리를 위해서 예외처리
            catch(ObjectOptimisticLockingFailureException e){
                log.warn("rp update lock conflict", e);


            }
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void insertLawmaker(LawmakerDfRequest ldr) {
        boolean partyUpdated = false;
        while (!partyUpdated) {
            try {
                String partyName = ldr.getPartyName();
                var party = partyRepository.findPartyByName(partyName).orElse(null);

                if (party == null) {
                    party = Party.create(partyName, ldr.getDistrict());
                } else {
                    // 정당 정보 업데이트
                    party.addCongressmanCount(ldr.getDistrict());
                }

                var congressman = Congressman.of(ldr, party);

                //party는 flush가 되는게 좋으므로 가독성을 위해 flush처리
                partyRepository.saveAndFlush(party);
                congressmanRepository.save(congressman);
                partyUpdated = true;
            } catch (ObjectOptimisticLockingFailureException e) {
                log.warn("party update lock conflict", e);

            }
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void lawmakerStateToFalse(Congressman lawmaker) {
        boolean partyUpdated = false;
        boolean congressmanUpdate = false;
        while (!partyUpdated & !congressmanUpdate) {
            try {
                String partyName = lawmaker.getParty().getName();
                var party = partyRepository.findPartyByName(partyName).orElseThrow(
                        ()-> new PartyException.PartyNotFound(Map.of("party",partyName))
                );

                // 정당 정보 업데이트
                party.subCongressmanCount(lawmaker.getDistrict());
                lawmaker.updateState(false);

                //party는 flush가 되는게 좋으므로 가독성을 위해 flush처리
                partyRepository.saveAndFlush(party);
                congressmanRepository.save(lawmaker);
                partyUpdated = true;
                congressmanUpdate = true;

            } catch (ObjectOptimisticLockingFailureException e) {
                log.warn("party update lock conflict", e);

            }
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void lawmakerStateToTrue(LawmakerDfRequest ldr,Congressman congressmanStateUpdateTrue) {
        boolean partyUpdated = false;
        boolean congressmanUpdate = false;
        while (!partyUpdated & !congressmanUpdate) {
            try {
                //과거 정당에서 의원유형별 의원수 변경
                //의원 정보만 업데이트 할 수도 있음
                String partyName = congressmanStateUpdateTrue.getParty().getName();
                var party = partyRepository.findPartyByName(partyName).orElseThrow(
                        ()-> new PartyException.PartyNotFound(Map.of("party",partyName))
                );
                // 정당 정보 업데이트
                party.subCongressmanCount(congressmanStateUpdateTrue.getDistrict());
                if (partyName.equals(ldr.getPartyName())){
                    party.addCongressmanCount(ldr.getDistrict());
                    congressmanStateUpdateTrue.update(ldr, party);
                }
                else{
                    //달라진 정당에 의원 수 수정
                    var newParty = partyRepository.findPartyByName(ldr.getPartyName()).orElseThrow(
                            ()-> new PartyException.PartyNotFound(Map.of("party",partyName))
                    );
                    newParty.addCongressmanCount(ldr.getDistrict());
                    congressmanStateUpdateTrue.update(ldr, newParty);
                    partyRepository.saveAndFlush(newParty);

                }

                //의원 상태 변경
                congressmanStateUpdateTrue.updateState(true);

                //flush처리 되지만 가독성을 위해 flush처리
                partyRepository.saveAndFlush(party);
                congressmanRepository.save(congressmanStateUpdateTrue);
                partyUpdated = true;
                congressmanUpdate = true;

            } catch (ObjectOptimisticLockingFailureException e) {
                log.warn("party update lock conflict", e);

            }
        }
    }

}


