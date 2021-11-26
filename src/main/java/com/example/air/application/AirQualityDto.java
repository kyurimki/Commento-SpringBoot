package com.example.air.application;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

public class AirQualityDto {
    @Getter
    @Builder
    public static class GetAirQualityInfo {
        private String city;
        private Double cityPm10Avg;
        private AirQualityGrade cityPm10AvgGrade;
        private List<DistrictAirQualityInfo> districtList;

        public GetAirQualityInfo searchByDistrict(String district) {
            if(district == null) {
                return this;
            }
            var searchedDistrictInfo = searchDistrictAirQualityInfo(district);
            districtList = Collections.singletonList(searchedDistrictInfo);
            return this;
        }

        private DistrictAirQualityInfo searchDistrictAirQualityInfo(String district) {
            return districtList.stream()
                    .filter(districtAirQualityInfo -> districtAirQualityInfo.getDistrict().equals(district))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(district + "인 자치구가 존재하지 않습니다."));
        }
    }

    @Getter
    public static class DistrictAirQualityInfo {
        private final String district;
        private final Integer pm10;
        private final Integer pm25;
        private final Double o3;
        private final Double so2;
        private final Double no2;
        private final Double co;
        private final AirQualityGrade pm10Grade;
        private final AirQualityGrade pm25Grade;
        private final AirQualityGrade o3Grade;
        private final AirQualityGrade so2Grade;
        private final AirQualityGrade no2Grade;
        private final AirQualityGrade coGrade;

        public DistrictAirQualityInfo(String district, Integer pm10, Integer pm25, Double o3, Double so2, Double no2, Double co) {
            this.district = district;
            this.pm10 = pm10;
            this.pm25 = pm25;
            this.o3 = o3;
            this.so2 = so2;
            this.no2 = no2;
            this.co = co;
            this.pm10Grade = AirQualityGradeUtil.getPm10Grade(Double.valueOf(pm10));
            this.pm25Grade = AirQualityGradeUtil.getPm25Grade(Double.valueOf(pm25));
            this.o3Grade = AirQualityGradeUtil.getO3Grade(o3);
            this.so2Grade = AirQualityGradeUtil.getNo2Grade(no2);
            this.no2Grade = AirQualityGradeUtil.getCoGrade(co);
            this.coGrade = AirQualityGradeUtil.getSo2Grade(so2);
        }
    }
}
