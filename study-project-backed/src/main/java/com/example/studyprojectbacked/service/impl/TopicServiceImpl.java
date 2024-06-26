package com.example.studyprojectbacked.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.studyprojectbacked.entity.dto.*;
import com.example.studyprojectbacked.entity.vo.request.AddCommentVO;
import com.example.studyprojectbacked.entity.vo.request.TopicCreateVO;
import com.example.studyprojectbacked.entity.vo.request.TopicUpdateVO;
import com.example.studyprojectbacked.entity.vo.response.CommentVO;
import com.example.studyprojectbacked.entity.vo.response.TopicDetailVO;
import com.example.studyprojectbacked.entity.vo.response.TopicPreviewVO;
import com.example.studyprojectbacked.entity.vo.response.TopicTopVO;
import com.example.studyprojectbacked.mapper.*;
import com.example.studyprojectbacked.service.TopicService;
import com.example.studyprojectbacked.util.CacheUtils;
import com.example.studyprojectbacked.util.Const;
import com.example.studyprojectbacked.util.FlowUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {
	@Resource
	TopicTypeMapper typeMapper;
	@Resource
	FlowUtil flowUtil;
	@Resource
	TopicMapper topicMapper;
	@Resource
	CacheUtils cacheUtils;
	@Resource
	AccountMapper accountMapper;
	@Resource
	AccountDetailsMapper accountDetailsMapper;
	@Resource
	AccountPrivacyMapper accountPrivacyMapper;
	@Resource
	StringRedisTemplate stringRedisTemplate;
	@Resource
	TopicCommentMapper commentMapper;

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
		if (!this.textLimitCheck(vo.getContent(),20000))
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
			cacheUtils.deleteCachePattern(Const.FORUM_TOPIC_PREVIEW_CACHE + "*");
			return null;
		} else {
			return "内部错误，请联系管理员！";
		}
	}

	@Override
	public String updateTopic(int uid, TopicUpdateVO vo) {
		if (!this.textLimitCheck(vo.getContent(), 20000))
			return "文章内容太多，发文失败！";
		if (!types.contains(vo.getType()))
			return "文章类型非法！";
		Topic topic = this.asTopic(vo);
		topicMapper.updateTopicByIdAndUid(vo.getId(), uid, topic);
		return  null;
	}

	@Override
	public String createComment(int uid, AddCommentVO vo) {
		if (!this.textLimitCheck(JSONObject.parseObject(vo.getContent()), 2000))
			return "评论内容太多，发文失败！";
		String key = Const.FORUM_TOPIC_COMMENT_COUNTER + uid;
		if (!flowUtil.limitPeriodCounterCheck(key, 2, 60))
			return "发表评论频繁，请稍后再试！";
		TopicComment comment = new TopicComment();
		comment.setUid(uid);
		BeanUtils.copyProperties(vo,comment);
		comment.setTime(new Date());
		commentMapper.creatTopicComment(comment);
		return null;
	}

	@Override
	public List<CommentVO> comments(int tid, int pageNumber) {
		List<TopicComment> comments = commentMapper.commentList(tid, pageNumber * 10);
		return comments.stream().map(dto -> {
			CommentVO vo = new CommentVO();
			BeanUtils.copyProperties(dto, vo);
			if (dto.getQuote() > 0) {
				TopicComment comment = commentMapper.getCommentById(dto.getQuote());
				if (comment != null) {
					JSONObject object = JSONObject.parseObject(comment.getContent());
					StringBuilder builder = new StringBuilder();
					this.shortContent(object.getJSONArray("ops"), builder, ignore -> {});
					vo.setQuote(builder.toString());
				} else {
					vo.setQuote("此评论已被删除");
				}

			}
			CommentVO.User user = new CommentVO.User();
			this.fillUserDetailsByPrivacy(user,dto.getUid());
			vo.setUser(user);
			return vo;
		}).toList();
	}

	@Override
	public void deleteComment(int id, int uid) {
		commentMapper.deleteComment(id, uid);
	}

	private Topic asTopic( TopicUpdateVO vo) {
		Topic topic = new Topic();
		topic.setTitle(vo.getTitle());
		topic.setType(vo.getType());
		topic.setContent(vo.getContent().toString());
		return topic;
	}

	@Override
	public List<TopicPreviewVO> listTopicCollects(int uid) {
		return topicMapper.collectTopic(uid)
				.stream()
				.map(topic -> {
					TopicPreviewVO vo = new TopicPreviewVO();
					BeanUtils.copyProperties(topic, vo);
					return vo;
				}).toList();
	}

	@Override
	public List<TopicPreviewVO> listTopicByPage(int page, int type) {
		String key = Const.FORUM_TOPIC_PREVIEW_CACHE + page + ":" + type;
		List<TopicPreviewVO> list = cacheUtils.takeListFromCache(key, TopicPreviewVO.class);
		if (list != null) return list;
		List<Topic> topics;
		if (type == 0)
			topics = topicMapper.topicList(page * 10);
		else
			topics = topicMapper.topicListByType(page * 10, type);
		if (topics.isEmpty()) return null;
		list = topics.stream().map(this::resolveToPreview).toList();
		cacheUtils.saveListToCache(key, list,60);
		return list;
	}

	@Override
	public List<TopicTopVO> listTopTopics() {
		List<Topic> topics = topicMapper.getTopicByTop();
		return topics.stream().map(topic -> {
			TopicTopVO vo = new TopicTopVO();
			BeanUtils.copyProperties(topic,vo);
			return vo;
		}).toList();
	}

	@Override
	public TopicDetailVO getTopic(int tid, int uid) {
		TopicDetailVO vo = new TopicDetailVO();
		Topic topic = topicMapper.getTopicById(tid);
		BeanUtils.copyProperties(topic,vo);
		TopicDetailVO.Interact interact = new TopicDetailVO.Interact(
				hasInteract(tid, uid, "like"),
				hasInteract(tid, uid,"collect")
		);
		vo.setInteract(interact);
		TopicDetailVO.User user = new TopicDetailVO.User();
		vo.setUser(this.fillUserDetailsByPrivacy(user,topic.getUid()));
		vo.setComments(commentMapper.commentCount(tid));
		return vo;
	}

	@Override
	public void interact(Interact interact, boolean state) {
		String type = interact.getType();
		synchronized (type.intern()) {
			stringRedisTemplate.opsForHash().put(type,interact.toKey(),Boolean.toString(state));
			this.saveInteractSchedule(type);
		}
	}

	private boolean hasInteract(int tid, int uid, String type) {
		String key = tid + ":" +uid;
		if (stringRedisTemplate.opsForHash().hasKey(type,key)) {
			return Boolean.parseBoolean(stringRedisTemplate.opsForHash().entries(type).get(key).toString());
		}
		return topicMapper.userInteractCount(tid, uid, type) > 0;
	}

	private final Map<String ,Boolean> state = new HashMap<>();
	ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
	private void saveInteractSchedule(String type) {
		if (!state.getOrDefault(type, false)) {
			state.put(type, true);
			service.schedule(() -> {
				this.saveInteract(type);
				state.put(type, false);
			}, 3, TimeUnit.SECONDS);
		}
	}

	private void saveInteract(String type) {
		synchronized (type.intern()) {
			List<Interact> check = new LinkedList<>();
			List<Interact> uncheck = new LinkedList<>();
			stringRedisTemplate.opsForHash().entries(type).forEach((k, v) -> {
				if (Boolean.parseBoolean(v.toString()))
					check.add(Interact.parseInteract(k.toString(), type));
				else
					uncheck.add(Interact.parseInteract(k.toString(), type));
			});
			if (!check.isEmpty())
				topicMapper.addInteract(check,type);
			else
				topicMapper.deleteInteract(uncheck,type);
			stringRedisTemplate.delete(type);
		}
	}

	private <T> T fillUserDetailsByPrivacy(T target, int uid){
		AccountDetails details = accountDetailsMapper.findAccountDetailsById(uid);
		Account account = accountMapper.getAccountById(uid);
		AccountPrivacy privacy = accountPrivacyMapper.getAccountPrivacyById(uid);
		String[] ignores = privacy.hiddenFields();
		BeanUtils.copyProperties(account,target,ignores);
		BeanUtils.copyProperties(details,target,ignores);
		return target;
	}

	private TopicPreviewVO resolveToPreview(Topic topic){
		TopicPreviewVO vo = new TopicPreviewVO();
		BeanUtils.copyProperties(accountMapper.getAccountById(topic.getUid()), vo);
		BeanUtils.copyProperties(topic,vo);
		vo.setLike(topicMapper.interactCount(topic.getId(),"like"));
		vo.setCollect(topicMapper.interactCount(topic.getId(), "collect"));
		List<String> images = new ArrayList<>();
		StringBuilder previewText = new StringBuilder();
		JSONArray ops = JSONObject.parseObject(topic.getContent()).getJSONArray("ops");
		this.shortContent(ops, previewText, obj -> images.add(obj.toString()));
		vo.setText(previewText.length() > 300 ? previewText.substring(0,300) : previewText.toString());
		vo.setImages(images);
		return vo;
	}

	private void shortContent(JSONArray ops, StringBuilder previewText, Consumer<Object> imageHander){
		for (Object op : ops) {
			Object insert = JSONObject.from(op).get("insert");
			if (insert instanceof String text) {
				if (previewText.length() >= 300) continue;
				previewText.append(text);
			} else if (insert instanceof Map<?,?> map) {
				Optional.ofNullable(map.get("image"))
						.ifPresent(imageHander);
			}
		}
	}

	private boolean textLimitCheck(JSONObject object, int max) {
		if (object == null) return false;
		long length = 0;
		for (Object op : object.getJSONArray("ops")) {
			length += JSONObject.from(op).getString("insert").length();
			if (length > max) return false;
		}
		return true;
	}
}
