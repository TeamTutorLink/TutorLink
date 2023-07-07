package com.tutorlink.api.lesson.controller;

import com.tutorlink.api.lesson.dto.request.AddLessonReq;
import com.tutorlink.api.lesson.dto.request.GetLessonListReq;
import com.tutorlink.api.lesson.dto.request.SearchLessonReq;
import com.tutorlink.api.lesson.dto.request.UpdateLessonReq;
import com.tutorlink.api.lesson.dto.response.GetLessonListRes;
import com.tutorlink.api.lesson.dto.response.SearchLessonRes;
import com.tutorlink.api.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

// TODO 유저 정보 받는 부분 추가해야함
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

    @GetMapping
    public ResponseEntity<Object> getLessonList(@RequestBody @Valid GetLessonListReq req) {

        List<GetLessonListRes> resList = lessonService.getLessonList(req);

        return ResponseEntity.ok().body(resList);
    }

    @GetMapping("/{lessonId}/image-file")
    public ResponseEntity<Object> downloadImageFile(@PathVariable @Min(1) int lessonId) throws MalformedURLException {

        HashMap<String, Object> hashMap = lessonService.downloadImageFile(lessonId);

        UrlResource res = (UrlResource) hashMap.get("urlResource");
        String ext = (String) hashMap.get("ext");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/" + ext)
                .body(res);
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable @Min(1) int lessonId,
                                               @RequestPart @Valid UpdateLessonReq req,
                                               @RequestPart @Nullable MultipartFile imageFile) {

        lessonService.updateLesson(lessonId, req, imageFile);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable @Min(1) int lessonId) {

        lessonService.deleteLesson(lessonId);

        return ResponseEntity.ok().build();
    }

    // TODO 강의 좋아요
    @PostMapping("/{lessonId}/like")
    public ResponseEntity<Object> likeLesson(@PathVariable @Min(1) int lessonId) {

        lessonService.likeLesson(lessonId);

        return ResponseEntity.ok().build();
    }

    // TODO 강의 좋아요 취소
    @PostMapping("/{lessonId}/cancel-like")
    public ResponseEntity<Object> cancelLikeLesson(@PathVariable @Min(1) int lessonId) {

        lessonService.cancelLikeLesson(lessonId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchLesson(@RequestBody @Valid SearchLessonReq req) {

        List<SearchLessonRes> resList = lessonService.searchLesson(req);

        return ResponseEntity.ok().body(resList);
    }

}
