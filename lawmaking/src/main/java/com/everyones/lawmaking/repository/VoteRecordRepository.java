package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.VoteRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRecordRepository extends JpaRepository<VoteRecord,Long> {
    @Query("select vr.id from VoteRecord vr " +
            "join vr.bill b " +
            "where b.id =:billId ")
    Optional<Long> findVoteRecordIdByBillId(@Param("billId") String billId);

    @Query("select vr from VoteRecord vr " +
            "where vr.bill.id = :billId ")
    Optional<VoteRecord> findVoteRecordByBillId(@Param("billId") String billId);
}