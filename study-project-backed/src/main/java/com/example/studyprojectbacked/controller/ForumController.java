package com.example.studyprojectbacked.controller;

import com.example.studyprojectbacked.entity.RestBeen;
import com.example.studyprojectbacked.entity.vo.response.WeatherVO;
import com.example.studyprojectbacked.service.WeatherService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forum")
public class ForumController {
	@Resource
	WeatherService weatherService;
	@GetMapping("/weather")
	public RestBeen<WeatherVO> weather(double longitude, double latitude){
		WeatherVO vo = weatherService.fetchWeather(longitude, latitude);
		return vo == null ?
				RestBeen.failure(400,"获取地理位置信息与天气失败，请联系管理人员！") : RestBeen.success(vo);
	}
}
