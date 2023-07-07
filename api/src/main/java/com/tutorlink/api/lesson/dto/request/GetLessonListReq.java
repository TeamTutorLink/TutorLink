package com.tutorlink.api.lesson.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class GetLessonListReq {
    @NotNull
    @Min(1)
    int page;
}
