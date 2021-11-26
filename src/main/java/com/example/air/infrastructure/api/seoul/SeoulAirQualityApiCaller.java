package com.example.air.infrastructure.api.seoul;

import com.example.air.application.AirQualityDto;
import com.example.air.application.AirQualityGradeUtil;
import com.example.air.application.City;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SeoulAirQualityApiCaller {
    private final SeoulAirQualityApi seoulAirQualityApi;

    public SeoulAirQualityApiCaller(@Value("${api.seoul.base-url}") String baseUrl) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        this.seoulAirQualityApi = retrofit.create(SeoulAirQualityApi.class);
    }

    public AirQualityDto.GetAirQualityInfo getAirQuality() {
        try {
            var call = seoulAirQualityApi.getAirQuality();
            var response = call.execute().body();

            if (response == null || response.getResponse() == null) {
                throw new RuntimeException("getAirQuality 응답값이 존재하지 않습니다.");
            }

            if (response.getResponse().isSuccess()) {
                log.info(response.toString());
                return convertToCommon(response);
            }

            throw new RuntimeException("getAirQuality 응답이 올바르지 않습니다. header=" + response.getResponse().getResult());

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("getAirQuality API error 발생! errorMessage=" + e.getMessage());
        }
    }

    private AirQualityDto.GetAirQualityInfo convertToCommon(SeoulAirQualityApiDto.GetAirQualityInfo response) {
        var elements = response.getResponse().getElements();
        var citypm10Avg = averagePm10(elements);
        var cityPm10AvgGrade = AirQualityGradeUtil.getPm10Grade(citypm10Avg);
        var districtList = splitByDistrict(elements);

        return AirQualityDto.GetAirQualityInfo.builder()
                .city(City.seoul.getDescription())
                .cityPm10Avg(citypm10Avg)
                .cityPm10AvgGrade(cityPm10AvgGrade)
                .districtList(districtList)
                .build();
    }

    private Double averagePm10(List<SeoulAirQualityApiDto.Element> elements) {
        return elements.stream()
                .mapToInt(SeoulAirQualityApiDto.Element::getPm10)
                .average()
                .orElse(Double.NaN);
    }

    private List<AirQualityDto.DistrictAirQualityInfo> splitByDistrict(List<SeoulAirQualityApiDto.Element> elements) {
        return elements.stream()
                .map(e -> new AirQualityDto.DistrictAirQualityInfo(
                        e.getDistrict(),
                        e.getPm10(),
                        e.getPm25(),
                        e.getO3(),
                        e.getNo2(),
                        e.getCo(),
                        e.getSo2())
                )
                .collect(Collectors.toList());
    }




}
