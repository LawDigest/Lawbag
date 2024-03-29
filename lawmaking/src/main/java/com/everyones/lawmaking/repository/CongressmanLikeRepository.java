package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.CongressManLike;
import com.everyones.lawmaking.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CongressmanLikeRepository extends JpaRepository<CongressManLike, Long> {

    @Query("SELECT cl FROM CongressManLike cl " +
            "WHERE cl.user.id = :userId AND cl.congressman.id = :congressmanId")
    Optional<CongressManLike> findByUserIdAndCongressmanId(@Param("userId")long userId, @Param("congressmanId")String congressmanId);

    @Query("SELECT cl.user FROM CongressManLike cl " +
            "WHERE cl.congressman.id = :congressmanId")
    List<User> findAllByCongressmanId(@Param("congressmanId") String congressmanId);



}
