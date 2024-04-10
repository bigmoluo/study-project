package com.example.studyprojectbacked.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.example.studyprojectbacked.entity.dto.Topic;
import com.example.studyprojectbacked.entity.dto.TopicType;
import com.example.studyprojectbacked.entity.vo.request.TopicCreateVO;
import com.example.studyprojectbacked.mapper.TopicMapper;
import com.example.studyprojectbacked.mapper.TopicTypeMapper;
import com.example.studyprojectbacked.service.TopicService;
import com.example.studyprojectbacked.util.Const;
import com.example.studyprojectbacked.util.FlowUtil;
import com.fasterxml.jackson.databind.util.BeanUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {
	@Resource
	TopicTypeMapper typeMapper;
	@Resource
	FlowUtil flowUtil;
	@Resource
	TopicMapper topicMapper;

	private Set<Integer> types = null;

	@PostConstruct
	private void initTypes(){
		types = this.listType()
				.stream()
				.map(TopicType::getId)
				.collect(Collectors.toSet());
	}
	@Override
	public List<TopicType> listType() {
		return typeMapper.getTopicTypeList();
	}

	@Override
	public String createTopic(TopicCreateVO vo, int uid) {
		if (!this.textLimitCheck(vo.getContent()))
			return "文章内容太多，发文失败！";
		if (!types.contains(vo.getType()))
			return "文章类型非法！";
		String key = Const.FORUM_TOPIC_CREATE_COUNTER + uid;
		if (!flowUtil.limitPeriodCounterCheck(key, 3, 3600))
			return "发文频繁，请稍后再试！";
		Topic topic = new Topic();
		BeanUtils.copyProperties(vo,topic);
		topic.setContent(vo.getContent().toJSONString());
		topic.setUid(uid);
		topic.setTime(new Date());
		if (topicMapper.saveTopic(topic)) {
			return null;
		} else {
			return "内部错误，请联系管理员！";
		}
	}

	private boolean textLimitCheck(JSONObject object) {
		if (object == null) return false;
		long length = 0;
		for (Object op : object.getJSONArray("ops")) {
			length += JSONObject.from(op).getString("insert").length();
			if (length > 20000) return false;
		}
		return true;
	}
}
