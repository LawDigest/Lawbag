package com.everyones.lawmaking.repository;

import com.everyones.lawmaking.common.dto.response.CityNameResponse;
import com.everyones.lawmaking.common.dto.response.DistrictNameResponse;
import com.everyones.lawmaking.common.dto.response.DistrictResponse;
import com.everyones.lawmaking.common.dto.response.GuNameResponse;
import com.everyones.lawmaking.domain.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District,Long > {
    @Query("select distinct d.cityName " +
            "from District d")
    List<String> findAllCity();

    @Query("select distinct d.guName " +
            "from District d " +
            "where d.cityName =:cityName ")
    List<String> findAllGu(@Param("cityName") String cityName);


    @Query("select distinct d.districtName " +
            "from District d " +
            "where d.cityName =:cityName and d.guName =:guName")
    List<String> findAllDistrict(@Param("cityName") String cityName, @Param("guName") String guName);
}
