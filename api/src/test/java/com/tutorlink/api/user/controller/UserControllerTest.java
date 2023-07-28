package com.tutorlink.api.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorlink.api.user.dto.request.UpdateUserReq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class UserControllerTest {

    private String JWT_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjksImlhdCI6MTY5MDUyMDI3NSwiZXhwIjoxNjkwNTIyMDc1fQ._xD9TjLoDT9z-DpiO7QDwaF9th9fYprUsjakXVzSXWI";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void changeToTeacher() throws Exception {
        mockMvc.perform(post("/users/change-to-teacher")
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("changeToTeacher",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void updateUser() throws Exception {
        UpdateUserReq req = new UpdateUserReq();
        req.setUserName("바꿀 이름");
        req.setIntroduction("소개글");

        MockMultipartFile reqFile = new MockMultipartFile("req", null, "application/json", objectMapper.writeValueAsString(req).getBytes(StandardCharsets.UTF_8));

        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "img.png", null, "{imageFile}".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(HttpMethod.PUT, "/users")
                        .file(reqFile)
                        .file(imageFile)
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("updateUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/users/logout")
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("logout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users")
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("deleteUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}