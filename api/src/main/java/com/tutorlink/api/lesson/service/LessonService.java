package com.tutorlink.api.lesson.service;

import com.tutorlink.api.lesson.domain.Lesson;
import com.tutorlink.api.lesson.dto.request.AddLessonReq;
import com.tutorlink.api.lesson.dto.request.GetLessonListReq;
import com.tutorlink.api.lesson.dto.request.SearchLessonReq;
import com.tutorlink.api.lesson.dto.request.UpdateLessonReq;
import com.tutorlink.api.lesson.dto.response.GetLessonListRes;
import com.tutorlink.api.lesson.dto.response.SearchLessonRes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

public interface LessonService {
    Lesson addLesson(AddLessonReq req, MultipartFile imageFile) throws IOException, NoSuchAlgorithmException;

    List<GetLessonListRes> getLessonList(GetLessonListReq req);

    List<SearchLessonRes> searchLesson(SearchLessonReq req);

    void updateLesson(int lessonId, UpdateLessonReq req, MultipartFile imageFile);

    HashMap<String, Object> downloadImageFile(int lessonId) throws MalformedURLException;

    void deleteLesson(int lessonId);

    void likeLesson(int lessonId);

    void cancelLikeLesson(int lessonId);
}
