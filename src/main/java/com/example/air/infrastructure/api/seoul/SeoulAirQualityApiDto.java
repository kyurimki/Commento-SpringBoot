package com.example.air.infrastructure.api.seoul;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

public class SeoulAirQualityApiDto {
    @Getter
    @Setter
    @ToString
    public static class GetAirQualityInfo {
        @JsonProperty("DailyAverageCityAir")
        private Response response;
    }

    @Getter
    @Setter
    @ToString
    public static class Response {
        @JsonProperty("list_total_count")
        private Integer totalCount;
        @JsonProperty("RESULT")
        private Result result;
        @JsonProperty("row")
        private List<Element> elements;

        public boolean isSuccess() {
            if (Objects.equals(result.getCode(), "INFO-000")) {
                return true;
            }
            return false;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class Result {
        @JsonProperty("CODE")
        private String code;
        @JsonProperty("MESSAGE")
        private String message;
    }

    @Getter
    @Setter
    @ToString
    public static class Element {
        @JsonProperty("MSRDT_DE")
        private String measuredDate;
        @JsonProperty("MSRSTE_NM")
        private String district;
        @JsonProperty("PM10")
        private Integer pm10;
        @JsonProperty("PM25")
        private Integer pm25;
        @JsonProperty("O3")
        private Double o3;
        @JsonProperty("NO2")
        private Double no2;
        @JsonProperty("CO")
        private Double co;
        @JsonProperty("SO2")
        private Double so2;
    }
}
