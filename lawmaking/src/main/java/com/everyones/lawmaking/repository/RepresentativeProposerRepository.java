package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.RepresentativeProposer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepresentativeProposerRepository extends JpaRepository<RepresentativeProposer,Long> {
}
