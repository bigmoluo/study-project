package com.example.studyprojectbacked.controller.exception;

import com.example.studyprojectbacked.entity.RestBeen;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@ControllerAdvice
@RestController
public class ValidationController {

    @ExceptionHandler(ValidationException.class)
    public RestBeen validateException(ValidationException exception){
        log.warn("Resolve [{}: {}]",exception.getClass().getName(),exception.getMessage());
        return RestBeen.failure(400,"请求参数有误");
    }
}
