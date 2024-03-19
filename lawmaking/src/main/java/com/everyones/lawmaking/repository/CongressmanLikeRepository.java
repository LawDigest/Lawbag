package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.CongressManLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CongressmanLikeRepository extends JpaRepository<CongressManLike, Long> {

}
