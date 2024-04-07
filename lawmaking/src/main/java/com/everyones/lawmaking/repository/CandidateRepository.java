package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.Candidate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    @Query(value = "select * " +
            "from " +
            "(select *,\n" +
            "         (\n" +
            "    (CASE WHEN city_name LIKE CONCAT('%',:keyword,'%') THEN 1\n" +
            "        WHEN district_name LIKE CONCAT('%',:keyword,'%') THEN 1\n" +
            "    WHEN gu_name LIKE CONCAT('%',:keyword,'%') THEN 1\n" +
            "            WHEN party_name LIKE CONCAT('%',:keyword,'%') THEN 1\n" +
            "            WHEN name LIKE CONCAT('%',:keyword,'%') THEN 1\n" +
            "        ELSE 0 END)\n" +
            "  ) AS relevance_score\n" +
            "from Candidate ) search\n" +
            "where relevance_score > 0\n;", nativeQuery = true)
    Slice<Candidate> findCandidateByKeyword(Pageable pageable, @Param("keyword") String keyword);
}
