package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.CongressManLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CongressmanLikeRepository extends JpaRepository<CongressManLike, Long> {

    @Query("SELECT cl FROM CongressManLike cl " +
            "WHERE cl.user.id = :userId AND cl.congressMan.id = :congressmanId")
    Optional<CongressManLike> findByUserIdAndCongressmanId(@Param("userId")long userId, @Param("congressmanId")String congressmanId);

}
