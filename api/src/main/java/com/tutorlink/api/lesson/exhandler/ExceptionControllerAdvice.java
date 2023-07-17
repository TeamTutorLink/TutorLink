package com.tutorlink.api.lesson.exhandler;


import com.tutorlink.api.common.error.ErrorResponse;
import com.tutorlink.api.lesson.exception.ImageNotFoundException;
import com.tutorlink.api.lesson.exception.LessonNotFoundException;
import com.tutorlink.api.lesson.exception.NotTeacherException;
import com.tutorlink.api.lesson.exception.UserNotMatchingException;
import com.tutorlink.api.user.exception.UserNotFoundException;
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse UserNotFoundExceptionHandle(UserNotFoundException e) {
        return new ErrorResponse("404", "사용자를 찾을 수 없습니다");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse LessonNotExistExceptionHandle(LessonNotFoundException e) {
        return new ErrorResponse("404", "강의를 찾을 수 없습니다");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse ImageNotExistExceptionHandle(ImageNotFoundException e) {
        return new ErrorResponse("404", "이미지 파일을 찾을 수 없습니다");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse exceptionHandle(Exception e) {
        return new ErrorResponse("500", "서버 에러");
    }
}
