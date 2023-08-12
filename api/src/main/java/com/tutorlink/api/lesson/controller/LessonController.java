package com.tutorlink.api.lesson.controller;

import com.tutorlink.api.auth.annotation.LoginRequired;
import com.tutorlink.api.lesson.dto.request.AddLessonReq;
import com.tutorlink.api.lesson.dto.request.UpdateLessonReq;
import com.tutorlink.api.lesson.dto.response.*;
import com.tutorlink.api.lesson.exception.ImageNotFoundException;
import com.tutorlink.api.lesson.exception.LessonNotFoundException;
import com.tutorlink.api.lesson.exception.NotTeacherException;
import com.tutorlink.api.lesson.exception.UserNotMatchingException;
import com.tutorlink.api.lesson.service.LessonService;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.exception.UserNotFoundException;
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

        User user = (User) servletRequest.getAttribute("user");
        lessonService.addLesson(user, req, imageFile);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login")
    @LoginRequired
    public ResponseEntity<Object> getLessonListLogin(HttpServletRequest servletRequest,
                                                     @RequestParam int page) {

        User user = (User) servletRequest.getAttribute("user");
        List<GetLessonListLoginRes> resList = lessonService.getLessonListLogin(user, page);

        return ResponseEntity.ok(resList);
    }

    @GetMapping
    public ResponseEntity<Object> getLessonList(@RequestParam int page) {

        List<GetLessonListRes> resList = lessonService.getLessonList(page);

        return ResponseEntity.ok(resList);
    }

    @GetMapping("/popular/login")
    @LoginRequired
    public ResponseEntity<Object> getPopularLessonListLogin(HttpServletRequest servletRequest,
                                                     @RequestParam int page) {

        User user = (User) servletRequest.getAttribute("user");
        List<GetPopularLessonListLoginRes> resList = lessonService.getPopularLessonListLogin(user, page);

        return ResponseEntity.ok(resList);
    }

    @GetMapping("/popular")
    public ResponseEntity<Object> getPopularLessonList(@RequestParam int page) {

        List<GetPopularLessonListRes> resList = lessonService.getPopularLessonList(page);

        return ResponseEntity.ok(resList);
    }

    @GetMapping("/{lessonId}/image-file")
    public ResponseEntity<Object> downloadImageFile(@PathVariable @Min(1) int lessonId) throws MalformedURLException, ImageNotFoundException, LessonNotFoundException {

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
                                                    @RequestParam int type,
                                                    @RequestParam String keyword,
                                                    @RequestParam int page) throws UserNotFoundException {

        User user = (User) servletRequest.getAttribute("user");
        List<SearchLessonLoginRes> resList = lessonService.searchLessonLogin(user, type, keyword, page);

        return ResponseEntity.ok().body(resList);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchLesson(@RequestParam int type,
                                               @RequestParam String keyword,
                                               @RequestParam int page) throws UserNotFoundException {

        List<SearchLessonRes> resList = lessonService.searchLesson(type, keyword, page);

        return ResponseEntity.ok().body(resList);
    }

    @PutMapping("/{lessonId}")
    @LoginRequired
    public ResponseEntity<Object> updateLesson(HttpServletRequest servletRequest,
                                               @PathVariable @Min(1) int lessonId,
                                               @RequestPart @Valid UpdateLessonReq req,
                                               @RequestPart @Nullable MultipartFile imageFile) throws UserNotMatchingException, IOException, NoSuchAlgorithmException, LessonNotFoundException {

        User user = (User) servletRequest.getAttribute("user");
        lessonService.updateLesson(user, lessonId, req, imageFile);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lessonId}")
    @LoginRequired
    public ResponseEntity<Object> deleteLesson(HttpServletRequest servletRequest,
                                               @PathVariable @Min(1) int lessonId) throws UserNotMatchingException, LessonNotFoundException {

        User user = (User) servletRequest.getAttribute("user");
        lessonService.deleteLesson(user, lessonId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{lessonId}/like")
    @LoginRequired
    public ResponseEntity<Object> likeLesson(HttpServletRequest servletRequest,
                                             @PathVariable @Min(1) int lessonId) throws LessonNotFoundException {

        User user = (User) servletRequest.getAttribute("user");
        lessonService.likeLesson(user, lessonId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{lessonId}/cancel-like")
    @LoginRequired
    public ResponseEntity<Object> cancelLikeLesson(HttpServletRequest servletRequest,
                                                   @PathVariable @Min(1) int lessonId) throws LessonNotFoundException {

        User user = (User) servletRequest.getAttribute("user");
        lessonService.cancelLikeLesson(user, lessonId);

        return ResponseEntity.ok().build();
    }
}
