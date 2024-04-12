package com.example.studyprojectbacked.mapper;

import com.example.studyprojectbacked.entity.dto.Interact;
import com.example.studyprojectbacked.entity.dto.Topic;
import org.apache.ibatis.annotations.*;

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

	@Insert("""
   			<script>
   				insert ignore into db_topic_interact_${type} values
   				<foreach collection = "interacts" item = "item" separator = ",">
   					(#{item.tid}, #{item.uid}, #{item.time})
   				</foreach>
   			</script>
			""")
	void addInteract(List<Interact> interacts, String type);

	@Delete("""
   			<script>
   				delete from db_topic_interact_${type} where
   				<foreach collection = "interacts" item = "item" separator = "or">
   					(tid = #{item.tid} and uid = #{item.uid})
   				</foreach>
   			</script>
			""")
	int deleteInteract(List<Interact> interacts, String type);

	@Select("select count(*) from db_topic_interact_${type} where tid = #{tid}")
	int interactCount(int tid, String type);

	@Select("select count(*) from db_topic_interact_${type} where tid = #{tid} and uid = #{uid}")
	int userInteractCount(int tid, int uid, String type);

	@Select("""
   			select * from db_topic_interact_collect left join db_topic on tid = db_topic.id
   			where db_topic_interact_collect.uid = #{uid}
			""")
	List<Topic> collectTopic(int uid);

	@Update("update db_topic set title = #{topic.title}, content = #{topic.content}, type = #{topic.type} " +
			"where uid = #{uid} and id = #{id}")
	void updateTopicByIdAndUid(int id, int uid, Topic topic);
}
