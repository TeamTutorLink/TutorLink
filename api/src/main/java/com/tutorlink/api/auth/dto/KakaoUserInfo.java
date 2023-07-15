package com.tutorlink.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class KakaoUserInfo {
    String id;

    String name;

    String email;

    @JsonProperty("properties")
    private void unpackProperties(HashMap<String, Object> properties) {
        name = (String) properties.get("nickname");
    }

    @JsonProperty("kakao_account")
    private void unpackKakaoAccount(HashMap<String, Object> kakaoAccount) {
        email = (String) kakaoAccount.get("email");
    }
}
