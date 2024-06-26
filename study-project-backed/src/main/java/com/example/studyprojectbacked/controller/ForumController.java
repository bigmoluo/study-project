package com.example.studyprojectbacked.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.studyprojectbacked.entity.RestBeen;
import com.example.studyprojectbacked.entity.dto.Interact;
import com.example.studyprojectbacked.entity.vo.request.AddCommentVO;
import com.example.studyprojectbacked.entity.vo.request.TopicCreateVO;
import com.example.studyprojectbacked.entity.vo.request.TopicUpdateVO;
import com.example.studyprojectbacked.entity.vo.response.*;
import com.example.studyprojectbacked.service.TopicService;
import com.example.studyprojectbacked.service.WeatherService;
import com.example.studyprojectbacked.util.Const;
import com.example.studyprojectbacked.util.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
	public RestBeen<TopicDetailVO> topic(@RequestParam @Min(0) int tid,
										 @RequestAttribute(Const.ATTR_USER_ID) int id){
		return RestBeen.success(topicService.getTopic(tid,id));
	}

	@GetMapping("/interact")
	public RestBeen<Void> interact(@RequestParam @Min(0) int tid,
								   @RequestParam @Pattern(regexp = "(like|collect)") String type,
								   @RequestParam boolean state,
								   @RequestAttribute(Const.ATTR_USER_ID) int id){
		topicService.interact(new Interact(tid, id, new Date(), type), state);
		return RestBeen.success();
	}

	@GetMapping("/collects")
	public RestBeen<List<TopicPreviewVO>> collects(@RequestAttribute(Const.ATTR_USER_ID) int id) {
		return RestBeen.success(topicService.listTopicCollects(id));
	}

	@PostMapping("/update-topic")
	public RestBeen<Void> updateTopic(@RequestBody @Valid TopicUpdateVO vo,
									  @RequestAttribute(Const.ATTR_USER_ID) int id) {
		return utils.messageHandle(() -> topicService.updateTopic(id, vo));
	}

	@PostMapping("/add-comment")
	public RestBeen<Void> addComment(@Valid @RequestBody AddCommentVO vo,
									 @RequestAttribute(Const.ATTR_USER_ID) int id){
		return utils.messageHandle(() -> topicService.createComment(id, vo));
	}

	@GetMapping("/comments")
	public RestBeen<List<CommentVO>> comments(@RequestParam @Min(0) int tid,
											  @RequestParam @Min(0) int page) {
		return RestBeen.success(topicService.comments(tid, page));
	}

	@GetMapping("/delete-comment")
	public RestBeen<Void> deleteComment(@RequestParam @Min(0) int id,
										@RequestAttribute(Const.ATTR_USER_ID) int uid){
		topicService.deleteComment(id, uid);
		return RestBeen.success();
	}
}
