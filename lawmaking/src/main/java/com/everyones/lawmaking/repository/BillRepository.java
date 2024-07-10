package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.Bill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, String> {

    // 단순한 법안 페이징으로 가져오기
    @Query("SELECT b FROM Bill b " +
            "ORDER BY b.proposeDate DESC, b.id DESC")
    Slice<Bill> findByPage(Pageable pageable);

    // 단계 + 법안 페이징으로 가져오기
    @Query("SELECT b FROM Bill b " +
           "WHERE b.stage = :stage " +
            "ORDER BY b.proposeDate DESC, b.id DESC ")
    Slice<Bill> findByPage(Pageable pageable, @Param("stage") String stage);

    // 특정 의원이 대표 발의한 법안들
    @Query("SELECT b FROM Bill b " +
           "JOIN FETCH b.representativeProposer rp " +
           "WHERE b.id = rp.bill.id " +
           "AND rp.congressman.id = :congressmanId ")
    Slice<Bill> findByRepresentativeProposer(String congressmanId, Pageable pageable);

    // 특정의원이 공동 발의한 법안들
    @Query("SELECT b FROM Bill b " +
            "WHERE exists (select bp FROM b.publicProposer bp where bp.congressman.id = :congressmanId)")
    Slice<Bill> findBillByPublicProposer(String congressmanId, Pageable pageable);

    // 정당 소속 의원들이 대표 발의한 법안
    @Query("SELECT b FROM Bill b " +
            "JOIN FETCH b.representativeProposer rp " +
            "JOIN rp.congressman c " +
            "JOIN c.party p " +
            "WHERE b.id = rp.bill.id " +
            "AND p.id = :partyId ")
    Slice<Bill> findRepresentativeBillsByParty(Pageable pageable, @Param("partyId") long partyId);

    // 정당 소속 의원들이 공동 발의한 법안
    // TODO: 쿼리 개선 필요
    @Query("SELECT b FROM Bill b " +
            "WHERE exists (select bp FROM b.publicProposer bp where bp.congressman.party.id = :partyId) " +
            "ORDER BY b.proposeDate DESC, b.id DESC")
    Slice<Bill> findPublicBillsByParty(Pageable pageable, @Param("partyId") long partyId);

    // 유저가 스크랩한 법안 페이징해서 가져오는 쿼리
    @Query("SELECT b FROM Bill b " +
            "JOIN FETCH b.representativeProposer rp " +
            "JOIN b.billLike bl " +
            "JOIN bl.user u " +
            "where u.id = :userId " +
            "order by bl.modifiedDate ")
    Slice<Bill> findByUserId(Pageable pageable, @Param("userId") long userId);

    // 단일 법안과 관련된 정보 가져오는 쿼리
    @Query("SELECT distinct b FROM Bill b " +
            "JOIN FETCH b.representativeProposer rp " +
            "JOIN FETCH rp.congressman rpc " +
            "JOIN FETCH rpc.party rpp " +
            "WHERE b.id = :billId "
    )
    Optional<Bill> findBillInfoById(String billId);

    // 피드 등 여러 법안들 가져오는 쿼리
    @Query("SELECT DISTINCT b FROM Bill b " +
            "JOIN FETCH b.publicProposer bp " +
            "JOIN FETCH bp.congressman bpc " +
            "JOIN FETCH bpc.party bpp " +
            "WHERE b.id in :billList "
    )
    List<Bill> findBillInfoByIdList(List<String> billList);

    // 유사한 법안 조회 법안과 같은 이름을 가진 법안 조회
    @Query("SELECT b FROM Bill b " +
            "JOIN FETCH b.representativeProposer rp " +
            "WHERE b.billName = :billName " +
            "AND b.id != :billId ")
    List<Bill> findSimilarBills(@Param("billName") String billName, @Param("billId") String billId);

    @Query(value = "select bill_id\n" +
            "from\n" +
            "(select bill_id ,match(bill_name) against(concat('*',:keyword,'*') in boolean mode) as bill_name_rel,\n" +
            "match(brief_summary) against(concat('*',:keyword,'*') in boolean mode) as brief_summary_rel,\n" +
            "match(gpt_summary) against(concat('*',:keyword,'*') in boolean mode) as gpt_summary_rel,\n" +
            "match(summary) against(concat('*',:keyword,'*') in boolean mode) as summary_rel\n" +
            "from Bill) search\n" +
            "where bill_name_rel >0 or brief_summary_rel>0 or gpt_summary_rel>0 or summary_rel > 0\n" +
            "order by bill_name_rel desc, brief_summary_rel desc, gpt_summary_rel desc, summary_rel desc ;"
            , nativeQuery = true)
    Slice<String> findBillByKeyword(Pageable pageable,@Param("keyword") String keyword);


    Optional<Bill> findBillByBillNumber(long billNumber);


}
