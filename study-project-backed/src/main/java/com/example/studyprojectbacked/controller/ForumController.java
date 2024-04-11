package com.example.studyprojectbacked.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.studyprojectbacked.entity.RestBeen;
import com.example.studyprojectbacked.entity.vo.request.TopicCreateVO;
import com.example.studyprojectbacked.entity.vo.response.*;
import com.example.studyprojectbacked.service.TopicService;
import com.example.studyprojectbacked.service.WeatherService;
import com.example.studyprojectbacked.util.Const;
import com.example.studyprojectbacked.util.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/forum")
public class ForumController {
	@Resource
	WeatherService weatherService;
	@Resource
	TopicService topicService;
	@Resource
	ControllerUtils utils;
	@GetMapping("/weather")
	public RestBeen<WeatherVO> weather(double longitude, double latitude){
		WeatherVO vo = weatherService.fetchWeather(longitude, latitude);
		return vo == null ?
				RestBeen.failure(400,"获取地理位置信息与天气失败，请联系管理人员！") : RestBeen.success(vo);
	}

	@GetMapping("/types")
	public RestBeen<List<TopicTypeVO>> listType(){
		return RestBeen.success(topicService
				.listType()
				.stream()
				.map(type -> type.asViewObject(TopicTypeVO.class))
				.toList());
	}
	@PostMapping("/create-topic")
	public RestBeen<Void> createTopic(@RequestBody @Valid TopicCreateVO vo,
									  @RequestAttribute(Const.ATTR_USER_ID) int id){
		return utils.messageHandle(() -> topicService.createTopic(vo, id));
	}
	@GetMapping("/list-topic")
	public RestBeen<List<TopicPreviewVO>> listTopic(@RequestParam @Min(0) int page,
													@RequestParam @Min(0) int type){
		return RestBeen.success(topicService.listTopicByPage(page,type));
	}
	@GetMapping("/top-topic")
	public RestBeen<List<TopicTopVO>> topTopic() {
		return RestBeen.success(topicService.listTopTopics());
	}
	@GetMapping("/topic")
	public RestBeen<TopicDetailVO> topic(@RequestParam @Min(0) int tid){
		return RestBeen.success(topicService.getTopic(tid));
	}

}
