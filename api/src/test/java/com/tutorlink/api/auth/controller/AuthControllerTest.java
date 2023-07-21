package com.tutorlink.api.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorlink.api.auth.dto.request.KakaoLoginReq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class AuthControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void kakaoLogin() throws Exception {
        KakaoLoginReq req = new KakaoLoginReq();
        req.setCode("oEfNPcrueVTPXT5iju5cpKS5YRnaqp6Ji4qxsGf1YIqEpAz5tWO9UxXci62yAczSQ9FIJAoqJQ0AAAGJV_Wmxg");

        mockMvc.perform(post("/auth/kakao-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("kakaoLogin",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}