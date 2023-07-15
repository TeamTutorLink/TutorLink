package com.tutorlink.api.lesson.controller;

import com.tutorlink.api.auth.annotation.LoginRequired;
import com.tutorlink.api.lesson.dto.request.*;
import com.tutorlink.api.lesson.dto.response.GetLessonListLoginRes;
import com.tutorlink.api.lesson.dto.response.GetLessonListRes;
import com.tutorlink.api.lesson.dto.response.SearchLessonLoginRes;
import com.tutorlink.api.lesson.dto.response.SearchLessonRes;
import com.tutorlink.api.lesson.exception.NotTeacherException;
import com.tutorlink.api.lesson.exception.UserNotMatchingException;
import com.tutorlink.api.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
@Validated
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    @LoginRequired
    public ResponseEntity<Object> addLesson(HttpServletRequest servletRequest,
                                            @RequestPart @Valid AddLessonReq req,
                                            @RequestPart @Nullable MultipartFile imageFile) throws IOException, NoSuchAlgorithmException, NotTeacherException {

        int userId = (int) servletRequest.getAttribute("userId");
        lessonService.addLesson(userId, req, imageFile);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login")
    @LoginRequired
    public ResponseEntity<Object> getLessonListLogin(HttpServletRequest servletRequest,
                                                     @RequestBody @Valid GetLessonListLoginReq req) {

        int userId = (int) servletRequest.getAttribute("userId");
        List<GetLessonListLoginRes> resList = lessonService.getLessonListLogin(userId, req);

        return ResponseEntity.ok(resList);
    }

    @GetMapping
    public ResponseEntity<Object> getLessonList(@RequestBody @Valid GetLessonListReq req) {

        List<GetLessonListRes> resList = lessonService.getLessonList(req);

        return ResponseEntity.ok(resList);
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

    @GetMapping("/search/login")
    @LoginRequired
    public ResponseEntity<Object> searchLessonLogin(HttpServletRequest servletRequest,
                                                    @RequestBody @Valid SearchLessonLoginReq req) {

        int userId = (int) servletRequest.getAttribute("userId");
        List<SearchLessonLoginRes> resList = lessonService.searchLessonLogin(userId, req);

        return ResponseEntity.ok().body(resList);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchLesson(@RequestBody @Valid SearchLessonReq req) {

        List<SearchLessonRes> resList = lessonService.searchLesson(req);

        return ResponseEntity.ok().body(resList);
    }

    @PutMapping("/{lessonId}")
    @LoginRequired
    public ResponseEntity<Object> updateLesson(HttpServletRequest servletRequest,
                                               @PathVariable @Min(1) int lessonId,
                                               @RequestPart @Valid UpdateLessonReq req,
                                               @RequestPart @Nullable MultipartFile imageFile) throws UserNotMatchingException, IOException, NoSuchAlgorithmException {

        int userId = (int) servletRequest.getAttribute("userId");
        lessonService.updateLesson(userId, lessonId, req, imageFile);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lessonId}")
    @LoginRequired
    public ResponseEntity<Object> deleteLesson(HttpServletRequest servletRequest,
                                               @PathVariable @Min(1) int lessonId) throws UserNotMatchingException {

        int userId = (int) servletRequest.getAttribute("userId");
        lessonService.deleteLesson(userId, lessonId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{lessonId}/like")
    @LoginRequired
    public ResponseEntity<Object> likeLesson(HttpServletRequest servletRequest,
                                             @PathVariable @Min(1) int lessonId) {

        int userId = (int) servletRequest.getAttribute("userId");
        lessonService.likeLesson(userId, lessonId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{lessonId}/cancel-like")
    @LoginRequired
    public ResponseEntity<Object> cancelLikeLesson(HttpServletRequest servletRequest,
                                                   @PathVariable @Min(1) int lessonId) {

        int userId = (int) servletRequest.getAttribute("userId");
        lessonService.cancelLikeLesson(userId, lessonId);

        return ResponseEntity.ok().build();
    }
}
