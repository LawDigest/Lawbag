package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.Bill;
import com.everyones.lawmaking.domain.entity.BillProposer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillProposerRepository extends JpaRepository<BillProposer, Long> {
    List<BillProposer> findByBill(Bill bill);

    List<BillProposer> findByBillId(String billId);

    // BillIdList에 있는 각 BillId에 대응하는 Congressman 이름을 리스트로 묶어서 가져오기
    @Query("SELECT bp.bill.id AS billId, " +
            "       bp.congressman.name AS congressmanName " +
            "FROM BillProposer bp " +
            "INNER JOIN bp.congressman c " +
            "WHERE bp.isRepresent = false AND bp.bill.id IN :billIds " +
            "ORDER BY bp.bill.id, bp.congressman.name")
    List<Object[]> findCongressmanNamesByBillIdList(@Param("billIds") List<String> billIds);
}
