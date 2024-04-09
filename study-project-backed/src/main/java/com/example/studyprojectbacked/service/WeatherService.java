package com.example.studyprojectbacked.service;

import com.example.studyprojectbacked.entity.vo.response.WeatherVO;

public interface WeatherService {
	WeatherVO fetchWeather(double longitude, double latitude);
}
