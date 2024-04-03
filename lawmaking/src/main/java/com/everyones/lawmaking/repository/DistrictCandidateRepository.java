package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.common.dto.ProportionalCandidateListDto;
import com.everyones.lawmaking.common.dto.response.PaginationResponse;
import com.everyones.lawmaking.common.dto.response.ProportionalCandidateListResponse;
import com.everyones.lawmaking.domain.entity.DistrictCandidate;
import com.everyones.lawmaking.domain.entity.ProportionalCandidate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistrictCandidateRepository extends JpaRepository<DistrictCandidate,Long > {

    @Query("select dc " +
            "from DistrictCandidate dc " +
            "join FETCH dc.party p " +
            "where dc.id =:candidateId ")
    Optional<DistrictCandidate> findDistrictCandidateById(@Param("candidateId") Long candidateId);

    @Query("select dc " +
            "from DistrictCandidate dc " +
            "join FETCH dc.party p " +
            "where dc.district.id = :districtId ")
    Slice<DistrictCandidate> findDistrictCandidateByDistrictId(@Param("districtId") long districtId, Pageable pageable);
}
