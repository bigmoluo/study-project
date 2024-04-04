package com.example.studyprojectbacked.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import org.slf4j.MDC;

import java.util.Optional;

public record RestBeen<T> (long id, int code, T data, String message) {

    public static <T> RestBeen<T> success(T data){
        return new RestBeen<>(requestId(),200,data,"请求成功");
    }
    public static <T> RestBeen<T> success(){
        return success(null);
    }
    public static <T> RestBeen<T> failure(int code, String message){
        return new RestBeen<>(requestId(),code,null,message);
    }
    public static <T> RestBeen<T> unauthorized(String message){
        return failure(401, message);
    }
    public static <T> RestBeen<T> forbidden(String message){
        return RestBeen.failure(403, message);
    }
    public String asJsonString(){
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
    /**
     * 获取当前请求ID方便快速定位错误
     * @return ID
     */
    private static long requestId(){
        String requestId = Optional.ofNullable(MDC.get("reqId")).orElse("0");
        return Long.parseLong(requestId);
    }
}
