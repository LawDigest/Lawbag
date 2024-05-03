package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.domain.entity.Candidate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, String> {

    @Query("SELECT b FROM Bill b " +
            "ORDER BY b.proposeDate DESC, b.id DESC")
    Slice<Bill> findByPage(Pageable pageable);

    @Query("SELECT b FROM Bill b " +
           "WHERE b.stage = :stage " +
            "ORDER BY b.proposeDate DESC, b.id DESC ")
    Slice<Bill> findByPage(Pageable pageable, @Param("stage") String stage);


    @Query("SELECT b FROM Bill b " +
            "JOIN FETCH b.representativeProposer rp " +
            "WHERE exists (select bp FROM b.publicProposer bp where bp.congressman.id = :congressmanId)")
    Slice<Bill> findBillByPublicProposer(String congressmanId, Pageable pageable);

    @Query("SELECT b FROM Bill b " +
           "JOIN FETCH b.representativeProposer rp " +
           "WHERE b.id = rp.bill.id " +
           "AND rp.congressman.id = :congressmanId ")
    Slice<Bill> findByRepresentativeProposer(String congressmanId, Pageable pageable);

    @Query("SELECT b FROM Bill b " +
            "JOIN FETCH b.representativeProposer rp " +
            "JOIN rp.congressman c " +
            "JOIN c.party p " +
            "WHERE b.id = rp.bill.id " +
            "AND p.id = :partyId ")
    Slice<Bill> findRepresentativeBillsByParty(Pageable pageable, @Param("partyId") long partyId);

    @Query("SELECT b FROM Bill b " +
           "JOIN FETCH b.representativeProposer rp " +
            "WHERE exists (select bp FROM b.publicProposer bp where bp.congressman.party.id = :partyId) " +
            "ORDER BY b.proposeDate DESC, b.id DESC")
    Slice<Bill> findPublicBillsByParty(Pageable pageable, @Param("partyId") long partyId);


    @Query("SELECT b FROM Bill b " +
            "JOIN FETCH b.representativeProposer rp " +
            "JOIN FETCH b.publicProposer bp " +
            "JOIN FETCH rp.congressman rpc " +
            "JOIN FETCH bp.congressman bpc " +
            "JOIN FETCH rpc.party rpp " +
            "JOIN FETCH bpc.party bpp " +
            "WHERE b.id = :billId "
    )
    Optional<Bill> findBillInfoById(String billId);

    @Query("SELECT b FROM Bill b " +
            "JOIN FETCH b.representativeProposer rp " +
            "JOIN FETCH b.publicProposer bp " +
            "JOIN FETCH rp.congressman rpc " +
            "JOIN FETCH bp.congressman bpc " +
            "JOIN FETCH rpc.party rpp " +
            "JOIN FETCH bpc.party bpp " +
            "WHERE b.id in :billList"
    )
    List<Bill> findBillInfoByIdList(List<String> billList);


    // billName 인덱스 걸어줘야 할듯
    @Query("SELECT b FROM Bill b " +
            "WHERE b.billName = :billName")
    List<Bill> findSimilarBills(@Param("billName") String billName);

    @Query(value = "select bill_id\n" +
            "from\n" +
            "(select bill_id ,match(gpt_summary) against(concat('*',:keyword,'*') in boolean mode) as gpt_summary_rel,\n" +
            "match(keyword) against(concat('*',:keyword,'*') in boolean mode) as keyword_rel,\n" +
            "match(bill_name) against(concat('*',:keyword,'*') in boolean mode) as bill_name_rel\n" +
            "from Bill) search\n" +
            "where keyword_rel >0 or gpt_summary_rel>0 or bill_name_rel > 0\n" +
            "order by keyword_rel desc, bill_name_rel desc, gpt_summary_rel desc"
            , nativeQuery = true)
    Slice<String> findBillByKeyword(Pageable pageable,@Param("keyword") String keyword);


}
