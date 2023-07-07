package com.tutorlink.api.lesson.dto.request;

import com.tutorlink.api.lesson.enumeration.RoomType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class UpdateLessonReq {
    @NotBlank
    String title;

    String password;

    @NotNull
    RoomType roomType;
}
