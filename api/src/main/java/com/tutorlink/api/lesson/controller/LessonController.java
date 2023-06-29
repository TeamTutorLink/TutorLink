package com.tutorlink.api.lesson.controller;

import com.tutorlink.api.lesson.dto.request.AddLessonReq;
import com.tutorlink.api.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
@Validated
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity<Object> addLesson(@RequestPart @Valid AddLessonReq req,
                                            @RequestPart @Nullable MultipartFile imageFile) throws IOException, NoSuchAlgorithmException {

        lessonService.addLesson(req, imageFile);

        return ResponseEntity.ok().build();
    }
}
