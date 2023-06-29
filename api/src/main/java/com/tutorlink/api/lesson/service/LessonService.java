package com.tutorlink.api.lesson.service;

import com.tutorlink.api.lesson.domain.Lesson;
import com.tutorlink.api.lesson.dto.request.AddLessonReq;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface LessonService {
    Lesson addLesson(AddLessonReq req, MultipartFile imageFile) throws IOException, NoSuchAlgorithmException;
}
