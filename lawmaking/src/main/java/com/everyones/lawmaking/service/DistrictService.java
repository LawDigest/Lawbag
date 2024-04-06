package com.everyones.lawmaking.service;

import com.everyones.lawmaking.common.dto.response.CityNameResponse;
import com.everyones.lawmaking.common.dto.response.DistrictNameResponse;
import com.everyones.lawmaking.common.dto.response.DistrictResponse;
import com.everyones.lawmaking.common.dto.response.GuNameResponse;
import com.everyones.lawmaking.repository.DistrictCandidateRepository;
import com.everyones.lawmaking.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DistrictService {
    private final DistrictRepository districtRepository;
    public List<DistrictResponse> getAllCity(){
        var cityList = districtRepository.findAllCity();
        return cityList.stream().map(CityNameResponse::from).toList();
    }
    public List<DistrictResponse> getGuNameList(String cityName) {
        var guList = districtRepository.findAllGu(cityName);
        return guList.stream().map(GuNameResponse::from).toList();    }

    public List<DistrictResponse> getDistrictList(String cityName, String guName){
        var districtList = districtRepository.findAllDistrict(cityName,guName);
        return districtList.stream().map(DistrictNameResponse::from).toList();        }
}
