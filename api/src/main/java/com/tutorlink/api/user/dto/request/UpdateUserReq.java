package com.tutorlink.api.user.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class UpdateUserReq {
    @NotBlank
    String userName;
    String introduction;
}
