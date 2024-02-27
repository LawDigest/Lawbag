package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.domain.entity.AuthInfo;
import com.everyones.lawmaking.domain.entity.Provider;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {
    Optional<AuthInfo> findBySocialIdAndProvider(@NotNull String socialId, @NotNull Provider provider);

}