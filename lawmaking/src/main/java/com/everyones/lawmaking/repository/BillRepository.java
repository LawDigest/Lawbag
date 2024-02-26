package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.common.dto.response.BillDetailDto;
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
    @Query("SELECT b FROM Bill b " +
            "ORDER BY b.proposeDate DESC, b.id DESC")
    Slice<Bill> findDefaultBillsByPage(Pageable pageable);

    @Query("SELECT b FROM Bill b " +
           "WHERE b.stage = :stage " +
            "ORDER BY b.proposeDate DESC, b.id DESC ")
    Slice<Bill> findDefaultBillsByStage(Pageable pageable, @Param("stage") String stage);

    @Query("SELECT new com.everyones.lawmaking.common.dto.response.BillDetailDto(b.id, b.billName, b.proposers, b.gptSummary,  b.proposeDate, b.stage, c.name, c.id, p.name, p.id, c.congressmanImageUrl, b.keyword)" +
            "FROM Bill b " +
            "JOIN b.publicProposer bp " +
            "JOIN bp.congressman c " +
            "JOIN c.party p " +
            "WHERE bp.isRepresent = true AND b.id = :billId " +
            "AND p.id = c.party.id")
    BillDetailDto findBillDetailByBillId(@Param("billId") String billId);

    @Query("SELECT b FROM Bill b " +
           "JOIN FETCH b.publicProposer bp " +
           "JOIN FETCH b.representativeProposer rp " +
           "JOIN FETCH bp.congressman c " +
           "JOIN FETCH c.party p " +
           "WHERE b.id = :billId " )
    Optional<Bill> findBillDetailById(@Param("billId") String billId);


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


}
