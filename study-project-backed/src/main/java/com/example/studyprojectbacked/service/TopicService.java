package com.example.studyprojectbacked.service;

import com.example.studyprojectbacked.entity.dto.Interact;
import com.example.studyprojectbacked.entity.dto.TopicType;
import com.example.studyprojectbacked.entity.vo.request.TopicCreateVO;
import com.example.studyprojectbacked.entity.vo.request.TopicUpdateVO;
import com.example.studyprojectbacked.entity.vo.response.TopicDetailVO;
import com.example.studyprojectbacked.entity.vo.response.TopicPreviewVO;
import com.example.studyprojectbacked.entity.vo.response.TopicTopVO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TopicService {
	List<TopicType> listType();
	String createTopic(TopicCreateVO vo, int uid);
	List<TopicPreviewVO> listTopicByPage(int page, int type);
	List<TopicTopVO> listTopTopics();
	TopicDetailVO getTopic(int tid, int uid);
	void interact(Interact interact, boolean state);
	List<TopicPreviewVO> listTopicCollects(int uid);
	String updateTopic(int uid, TopicUpdateVO vo);
}
