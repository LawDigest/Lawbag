package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.Congressman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CongressmanRepository extends JpaRepository<Congressman, Long> {

    Congressman findByName(String name);
    List<Congressman> findByNameIn(String[] names);

}
