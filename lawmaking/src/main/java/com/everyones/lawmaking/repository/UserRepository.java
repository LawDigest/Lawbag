package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAuthInfo_Id(Long authInfoId);

    @Query("SELECT cl.user FROM CongressManLike cl " +
            "WHERE cl.congressman.id = :congressmanId")
    List<User> findAllByCongressmanId(@Param("congressmanId") String congressmanId);

    @Query("select bl.user from BillLike bl " +
            "where bl.bill.id = :billId")
    List<User> findAllByBillId(@Param("billId") String billId);

}
