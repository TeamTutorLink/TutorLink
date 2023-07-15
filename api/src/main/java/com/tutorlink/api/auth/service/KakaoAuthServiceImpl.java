package com.tutorlink.api.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorlink.api.auth.dto.KakaoTokenInfo;
import com.tutorlink.api.auth.dto.KakaoUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class KakaoAuthServiceImpl implements KakaoAuthService {


    @Value("${KAKAO_REST_API_KEY}")
    private String KAKAO_REST_API_KEY;

    @Value("${KAKAO_REDIRECT_URI}")
    private String KAKAO_REDIRECT_URI;

    @Override
    public KakaoTokenInfo sendCode(String code) throws Exception {
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id=" + KAKAO_REST_API_KEY);
        sb.append("&redirect_uri=" + KAKAO_REDIRECT_URI);
        sb.append("&code=" + code);
        bw.write(sb.toString());
        bw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        ObjectMapper om = new ObjectMapper();

        KakaoTokenInfo kakaoTokenInfo = om.readValue(result, KakaoTokenInfo.class);

        return kakaoTokenInfo;
    }

    @Override
    public KakaoUserInfo sendToken(String accessToken) throws Exception {
        String reqUrl = "https://kapi.kakao.com/v2/user/me";

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        ObjectMapper om = new ObjectMapper();

        KakaoUserInfo kakaoUserInfo = om.readValue(result, KakaoUserInfo.class);

        log.info(kakaoUserInfo.toString());

        return kakaoUserInfo;
    }
}
