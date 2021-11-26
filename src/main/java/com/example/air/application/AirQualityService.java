package com.example.air.application;

import com.example.air.infrastructure.api.seoul.SeoulAirQualityApiCaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirQualityService {
    private final SeoulAirQualityApiCaller seoulAirQualityApiCaller;

    public AirQualityDto.GetAirQualityInfo getAirQualityInfo(City city, String district) {
        if(city == City.seoul) {
            var airQualityInfo = seoulAirQualityApiCaller.getAirQuality();
            if(district != null) {
                return airQualityInfo.searchByDistrict(district);
            }
            return airQualityInfo;
        }

        throw new RuntimeException(city + " 대기질 정보는 준비 중입니다.");
    }
}
