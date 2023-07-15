package com.tutorlink.api.lesson.exhandler;


import com.tutorlink.api.common.error.ErrorResponse;
import com.tutorlink.api.lesson.exception.NotTeacherException;
import com.tutorlink.api.lesson.exception.UserNotMatchingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.tutorlink.api.lesson")
public class ExceptionControllerAdvice {


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ErrorResponse NotTeacherExceptionHandle(NotTeacherException e) {
        return new ErrorResponse("403", "강사 계정이 아닙니다");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ErrorResponse UserNotMatchingExceptionHandle(UserNotMatchingException e) {
        return new ErrorResponse("403", "다른 사용자입니다");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse exceptionHandle(Exception e) {
        return new ErrorResponse("500", "서버 에러");
    }
}
