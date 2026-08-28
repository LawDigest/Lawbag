package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.Committee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommitteeRepository extends JpaRepository<Committee,Long> {
    Optional<Committee> findByCommitteeName(String committeeName);
}
