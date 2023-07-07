package com.tutorlink.api.lesson.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class GetLessonListRes {
    // TODO 추가
    int lessonId;
    String title;
    String userName;
    boolean likeLesson;
    int likeCount;
}
