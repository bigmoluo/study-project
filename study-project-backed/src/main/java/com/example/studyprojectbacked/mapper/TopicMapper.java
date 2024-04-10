package com.example.studyprojectbacked.mapper;

import com.example.studyprojectbacked.entity.dto.Topic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TopicMapper {

	@Insert("insert into db_topic(title,content,uid,type,time) values (#{title},#{content},#{uid},#{type},#{time})")
	boolean saveTopic(Topic topic);
}
