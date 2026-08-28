package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.CommitteeCongressman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CommitteeCongressmanRepository extends JpaRepository<CommitteeCongressman, Long> {
    Optional<CommitteeCongressman> findByCommitteeIdAndCongressmanId(long committeeId, String congressmanId);
    @Query("SELECT c.congressman.id FROM CommitteeCongressman c WHERE c.committee.id = :committeeId")
    List<String> findCongressmanIdsByCommitteeId(@Param("committeeId") Long committeeId);
    @Modifying
    @Transactional
    @Query("""
        DELETE FROM CommitteeCongressman c
         WHERE c.committee.id = :committeeId
           AND c.congressman.id IN :congressmanIds
    """)
    void deleteByCommitteeIdAndCongressmanIdIn(
            @Param("committeeId") Long committeeId,
            @Param("congressmanIds") Collection<String> congressmanIds
    );
}
