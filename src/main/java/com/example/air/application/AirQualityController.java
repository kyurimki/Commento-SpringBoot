package com.example.air.application;

import com.example.air.infrastructure.api.seoul.SeoulAirQualityApiCaller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/daily-air-quality")
public class AirQualityController {
    private final AirQualityService airQualityService;

    @GetMapping("/{city}")
    public AirQualityDto.GetAirQualityInfo getAirQualityInfo(@PathVariable("city") City city, @RequestParam(required = false) String district) {
        return airQualityService.getAirQualityInfo(city, district);
    }
}
