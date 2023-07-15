package com.tutorlink.api.lesson.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class SearchLessonLoginReq {
    @NotNull
    @Range(min = 0, max = 1) // 0:제목, 1:강사이름
    int type;

    @NotBlank
    String keyword;

    @NotNull
    @Min(1)
    int page;
}
