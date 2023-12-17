package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
//    Party findByName(String name);

    @Query("SELECT p FROM Party p WHERE p.name = :name")
    Party findByName(String name);
    @Query("SELECT p FROM Party p WHERE p.id IN :ids")
    List<Party> findAllByIds(List<Long> ids);
    @Query("SELECT p FROM Party p WHERE p.id = :id")
    Optional<Party> findById(Long id);
}
