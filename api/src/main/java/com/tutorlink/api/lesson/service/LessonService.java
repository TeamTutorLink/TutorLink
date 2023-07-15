package com.tutorlink.api.lesson.service;

import com.tutorlink.api.lesson.domain.Lesson;
import com.tutorlink.api.lesson.dto.request.*;
import com.tutorlink.api.lesson.dto.response.GetLessonListLoginRes;
import com.tutorlink.api.lesson.dto.response.GetLessonListRes;
import com.tutorlink.api.lesson.dto.response.SearchLessonLoginRes;
import com.tutorlink.api.lesson.dto.response.SearchLessonRes;
import com.tutorlink.api.lesson.exception.NotTeacherException;
import com.tutorlink.api.lesson.exception.UserNotMatchingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

public interface LessonService {
    Lesson addLesson(int userId, AddLessonReq req, MultipartFile imageFile) throws IOException, NoSuchAlgorithmException, NotTeacherException;

    List<GetLessonListLoginRes> getLessonListLogin(int userId, GetLessonListLoginReq req);

    List<GetLessonListRes> getLessonList(GetLessonListReq req);

    HashMap<String, Object> downloadImageFile(int lessonId) throws MalformedURLException;

    List<SearchLessonLoginRes> searchLessonLogin(int userId, SearchLessonLoginReq req);

    List<SearchLessonRes> searchLesson(SearchLessonReq req);

    Lesson updateLesson(int userId, int lessonId, UpdateLessonReq req, MultipartFile imageFile) throws UserNotMatchingException, IOException, NoSuchAlgorithmException;

    void deleteLesson(int userId, int lessonId) throws UserNotMatchingException;

    void likeLesson(int userId, int lessonId);

    void cancelLikeLesson(int userId, int lessonId);
}
