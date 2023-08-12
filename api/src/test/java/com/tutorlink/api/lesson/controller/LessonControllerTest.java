package com.tutorlink.api.lesson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorlink.api.lesson.dto.request.AddLessonReq;
import com.tutorlink.api.lesson.dto.request.UpdateLessonReq;
import com.tutorlink.api.lesson.enumeration.RoomType;
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
class LessonControllerTest {

    private String JWT_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTY5MTgxNTE0NSwiZXhwIjoxNjkxODE2OTQ1fQ.lg5gyfOhbBFrb7x6bD6C8ny-LmfSugUF2IDMqx8H6WQ";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addLesson() throws Exception {
        AddLessonReq req = new AddLessonReq();
        req.setTitle("제목");
        req.setPassword("비밀번호");
        req.setRoomType(RoomType.PUBLIC);
        MockMultipartFile reqFile = new MockMultipartFile("req", null, "application/json", objectMapper.writeValueAsString(req).getBytes(StandardCharsets.UTF_8));

        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "img.png", null, "{imageFile}".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(HttpMethod.POST, "/lessons")
                        .file(reqFile)
                        .file(imageFile)
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("addLesson",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getLessonListLogin() throws Exception {
        mockMvc.perform(get("/lessons/login?page={page}", 1)
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("getLessonListLogin",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getLessonList() throws Exception {
        mockMvc.perform(get("/lessons?page={page}", 1))
                .andExpect(status().isOk())
                .andDo(document("getLessonList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getPopularLessonListLogin() throws Exception {
        mockMvc.perform(get("/lessons/popular/login?page={page}", 1)
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("getPopularLessonListLogin",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getPopularLessonList() throws Exception {
        mockMvc.perform(get("/lessons/popular?page={page}", 1))
                .andExpect(status().isOk())
                .andDo(document("getPopularLessonList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getMyLessonList() throws Exception {
        mockMvc.perform(get("/lessons/my?page={page}", 1)
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("getMyLessonList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getLikeLessonList() throws Exception {
        mockMvc.perform(get("/lessons/like?page={page}", 1)
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("getLikeLessonList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void downloadImageFile() throws Exception {
        mockMvc.perform(get("/lessons/{lessonId}/image-file", 1))
                .andExpect(status().isOk())
                .andDo(document("downloadImageFile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void searchLessonLogin() throws Exception {
        mockMvc.perform(get("/lessons/search/login?type={type}&keyword={keyword}&page={page}", 0, "제", 1)
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("searchLessonLogin",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

    }

    @Test
    void searchLesson() throws Exception {
        mockMvc.perform(get("/lessons/search?type={type}&keyword={keyword}&page={page}", 0, "제", 1))
                .andExpect(status().isOk())
                .andDo(document("searchLesson",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void updateLesson() throws Exception {
        UpdateLessonReq req = new UpdateLessonReq();
        req.setTitle("바꿀 제목");
        req.setPassword("바꿀 비밀번호");
        req.setRoomType(RoomType.PRIVATE);
        MockMultipartFile reqFile = new MockMultipartFile("req", null, "application/json", objectMapper.writeValueAsString(req).getBytes(StandardCharsets.UTF_8));

        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "img.png", null, "{imageFile}".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(HttpMethod.PUT, "/lessons/{lessonId}", 1)
                        .file(reqFile)
                        .file(imageFile)
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("updateLesson",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void deleteLesson() throws Exception {
        mockMvc.perform(delete("/lessons/{lessonId}", 1)
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("deleteLesson",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void likeLesson() throws Exception {
        mockMvc.perform(post("/lessons/{lessonId}/like", 1)
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("likeLesson",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void cancelLikeLesson() throws Exception {
        mockMvc.perform(post("/lessons/{lessonId}/cancel-like", 1)
                        .header("accessToken", JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("cancelLikeLesson",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}