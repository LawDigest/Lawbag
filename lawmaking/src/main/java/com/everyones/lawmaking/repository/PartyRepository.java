package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PartyRepository extends JpaRepository<Party, Long> {
//    Party findByName(String name);

    @Query("SELECT p FROM Party p WHERE p.name = :name")
    Party findByName(String name);
}
