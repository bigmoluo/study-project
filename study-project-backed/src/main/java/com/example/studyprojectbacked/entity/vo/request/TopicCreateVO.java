package com.example.studyprojectbacked.entity.vo.request;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.relational.core.sql.In;

@Data
public class TopicCreateVO {
	@Min(1)
	Integer type;
	@Length(min = 1, max = 30)
	String title;
	JSONObject content;
}
