package com.example.studyprojectbacked.mapper;

import com.example.studyprojectbacked.entity.dto.TopicComment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TopicCommentMapper {
	@Insert("insert into db_topic_comment(uid,tid,content,time,quote) " +
			"values (#{uid},#{tid},#{content},#{time},#{quote})")
	void creatTopicComment(TopicComment comment);

	@Select("""
			select * from db_topic_comment where tid = #{tid}
			limit ${start}, 10 
			""")
	List<TopicComment> commentList(int tid, int start);

	@Select("select * from db_topic_comment where id = ${id}")
	TopicComment getCommentById(int id);
	@Select("select count(*) from db_topic_comment where tid = #{tid}")
	long commentCount(int tid);
	@Delete("delete from db_topic_comment where id = #{id} and uid = #{uid};")
	void deleteComment(int id, int uid);
}
