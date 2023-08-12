package com.tutorlink.api.lesson.service;

import com.tutorlink.api.lesson.domain.Lesson;
import com.tutorlink.api.lesson.dto.request.AddLessonReq;
import com.tutorlink.api.lesson.dto.request.UpdateLessonReq;
import com.tutorlink.api.lesson.dto.response.*;
import com.tutorlink.api.lesson.exception.ImageNotFoundException;
import com.tutorlink.api.lesson.exception.LessonNotFoundException;
import com.tutorlink.api.lesson.exception.NotTeacherException;
import com.tutorlink.api.lesson.exception.UserNotMatchingException;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.exception.UserNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

public interface LessonService {
    Lesson addLesson(User user, AddLessonReq req, MultipartFile imageFile) throws IOException, NoSuchAlgorithmException, NotTeacherException;

    List<GetLessonListLoginRes> getLessonListLogin(User user, int page);

    List<GetLessonListRes> getLessonList(int page);

    List<GetPopularLessonListLoginRes> getPopularLessonListLogin(User user, int page);

    List<GetPopularLessonListRes> getPopularLessonList(int page);

    HashMap<String, Object> downloadImageFile(int lessonId) throws MalformedURLException, ImageNotFoundException, LessonNotFoundException;

    List<SearchLessonLoginRes> searchLessonLogin(User user, int type, String keyword, int page) throws UserNotFoundException;

    List<SearchLessonRes> searchLesson(int type, String keyword, int page) throws UserNotFoundException;

    Lesson updateLesson(User user, int lessonId, UpdateLessonReq req, MultipartFile imageFile) throws UserNotMatchingException, IOException, NoSuchAlgorithmException, LessonNotFoundException;

    void deleteLesson(User user, int lessonId) throws UserNotMatchingException, LessonNotFoundException;

    void likeLesson(User user, int lessonId) throws LessonNotFoundException;

    void cancelLikeLesson(User user, int lessonId) throws LessonNotFoundException;

    List<GetMyLessonListRes> getMyLessonList(User user, int page);

    List<GetLikeLessonListRes> getLikeLessonList(User user, int page);
}
