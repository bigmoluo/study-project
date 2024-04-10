package com.example.studyprojectbacked.util;

import com.example.studyprojectbacked.entity.RestBeen;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class ControllerUtils {

	public  <T> RestBeen<T> messageHandle(Supplier<String> action){
		String message = action.get();
		if (message == null){
			return RestBeen.success();
		} else {
			return RestBeen.failure(400, message);
		}
	}
}
