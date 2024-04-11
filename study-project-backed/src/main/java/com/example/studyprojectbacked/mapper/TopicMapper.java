package com.example.studyprojectbacked.mapper;

import com.example.studyprojectbacked.entity.dto.Topic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TopicMapper {

	@Insert("insert into db_topic(title,content,uid,type,time) values (#{title},#{content},#{uid},#{type},#{time})")
	boolean saveTopic(Topic topic);

	@Select("""
				select * from db_topic
				order by `time` desc limit ${start}, 10
			""")
	List<Topic> topicList(int start);
//	@Select("""
//				select * from db_topic left join db_account on uid = db_account.id
//				order by `time` desc limit ${start}, 10
//			""")
//	List<Topic> topicList(int start);

	@Select("""
				select * from db_topic
				where type = #{type}
				order by `time` desc limit ${start}, 10
			""")
	List<Topic> topicListByType(int start, int type);
//	@Select("""
//				select * from db_topic left join db_account on uid = db_account.id
//				where type = #{type}
//				order by `time` desc limit ${start}, 10
//			""")
//	List<Topic> topicListByType(int start, int type);

	@Select("select id,title,time from db_topic where top = 1")
	List<Topic> getTopicByTop();

	@Select("select * from db_topic where id = #{tid};")
	Topic getTopicById(int tid);
}
