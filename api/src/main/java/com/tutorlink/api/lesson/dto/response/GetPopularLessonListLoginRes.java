package com.tutorlink.api.lesson.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetPopularLessonListLoginRes {
    int lessonId;
    String title;
    String userName;
    boolean likeLesson;
    int likeCount;
}
