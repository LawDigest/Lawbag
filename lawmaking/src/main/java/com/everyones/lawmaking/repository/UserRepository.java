package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAuthInfo_Id(long authInfoId);

}
