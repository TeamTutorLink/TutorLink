package com.tutorlink.api.lesson.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetMyLessonListRes {
    int lessonId;
    String title;
    String userName;
    int likeCount;
}
