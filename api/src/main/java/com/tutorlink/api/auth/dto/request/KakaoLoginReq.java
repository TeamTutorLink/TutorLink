package com.tutorlink.api.auth.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoLoginReq {
    String code;
}
