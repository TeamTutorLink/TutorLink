package com.tutorlink.api.auth.service;


import com.tutorlink.api.auth.dto.KakaoTokenInfo;
import com.tutorlink.api.auth.dto.KakaoUserInfo;

public interface KakaoAuthService {
    //카카오 서버로 인증 코드를 보내서 액세스/리프레쉬 토큰을 받아오는 메서드
    KakaoTokenInfo sendCode(String code) throws Exception;

    //카카오 서버에 액세스 토큰을 보내서 사용자 정보를 받아오는 메서드
    KakaoUserInfo sendToken(String accessToken) throws Exception;
}
