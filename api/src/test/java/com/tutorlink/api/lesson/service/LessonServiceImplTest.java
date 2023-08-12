package com.tutorlink.api.lesson.service;

import com.tutorlink.api.lesson.domain.Lesson;
import com.tutorlink.api.lesson.dto.request.AddLessonReq;
import com.tutorlink.api.lesson.dto.request.UpdateLessonReq;
import com.tutorlink.api.lesson.dto.response.GetLessonListLoginRes;
import com.tutorlink.api.lesson.dto.response.GetLessonListRes;
import com.tutorlink.api.lesson.dto.response.GetPopularLessonListLoginRes;
import com.tutorlink.api.lesson.dto.response.GetPopularLessonListRes;
import com.tutorlink.api.lesson.enumeration.RoomType;
import com.tutorlink.api.lesson.exception.LessonNotFoundException;
import com.tutorlink.api.lesson.exception.NotTeacherException;
import com.tutorlink.api.lesson.exception.UserNotMatchingException;
import com.tutorlink.api.lesson.repository.LessonRepository;
import com.tutorlink.api.lesson.repository.UserLessonLikeRepository;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.enumeration.SocialLoginType;
import com.tutorlink.api.user.enumeration.UserType;
import com.tutorlink.api.user.exception.UserNotFoundException;
import com.tutorlink.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
class LessonServiceImplTest {

    private User user = null;
    private Lesson lesson = null;

    @Autowired
    LessonService lessonService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    UserLessonLikeRepository userLessonLikeRepository;

    @BeforeEach
    void clearRepository() {
        userRepository.deleteAll();
        lessonRepository.deleteAll();
        userLessonLikeRepository.deleteAll();

        User newUser = new User(SocialLoginType.KAKAO, "23712326", "노호준", UserType.TEACHER, new Date());
        User saveUser = userRepository.save(newUser);
        user = saveUser;

        Lesson newLesson = new Lesson("제목", RoomType.PUBLIC, 0, user);
        Lesson saveLesson = lessonRepository.save(newLesson);
        lesson = saveLesson;
    }

    @Test
    void addLesson() throws NotTeacherException, IOException, NoSuchAlgorithmException {
        AddLessonReq req = new AddLessonReq();
        req.setTitle("제목");
        req.setPassword("1234");
        req.setRoomType(RoomType.PUBLIC);
        Lesson saveLesson = lessonService.addLesson(user, req, null);
        assertThat(lessonRepository.findById(saveLesson.getLessonId()).isPresent()).isTrue();
    }

    @Test
    void getLessonListLogin() {
        List<GetLessonListLoginRes> res = lessonService.getLessonListLogin(user, 1);
        assertThat(res.size()).isEqualTo(1);
    }

    @Test
    void getLessonList() {
        List<GetLessonListRes> res = lessonService.getLessonList(1);
        assertThat(res.size()).isEqualTo(1);
    }

    @Test
    void getPopularLessonListLogin() {
        Lesson lesson1 = new Lesson("제목", RoomType.PUBLIC, 123, user);
        Lesson lesson2 = new Lesson("제목", RoomType.PUBLIC, 763, user);
        Lesson lesson3 = new Lesson("제목", RoomType.PUBLIC, 12, user);
        Lesson lesson4 = new Lesson("제목", RoomType.PUBLIC, 86, user);

        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);
        lessonRepository.save(lesson3);
        lessonRepository.save(lesson4);

        List<GetPopularLessonListLoginRes> res = lessonService.getPopularLessonListLogin(user, 1);
        assertThat(res.get(0).getLikeCount()).isEqualTo(763);
        assertThat(res.get(1).getLikeCount()).isEqualTo(123);
        assertThat(res.get(2).getLikeCount()).isEqualTo(86);
        assertThat(res.get(3).getLikeCount()).isEqualTo(12);
    }

    @Test
    void getPopularLessonList() {
        Lesson lesson1 = new Lesson("제목", RoomType.PUBLIC, 123, user);
        Lesson lesson2 = new Lesson("제목", RoomType.PUBLIC, 763, user);
        Lesson lesson3 = new Lesson("제목", RoomType.PUBLIC, 12, user);
        Lesson lesson4 = new Lesson("제목", RoomType.PUBLIC, 86, user);

        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);
        lessonRepository.save(lesson3);
        lessonRepository.save(lesson4);

        List<GetPopularLessonListRes> res = lessonService.getPopularLessonList(1);
        assertThat(res.get(0).getLikeCount()).isEqualTo(763);
        assertThat(res.get(1).getLikeCount()).isEqualTo(123);
        assertThat(res.get(2).getLikeCount()).isEqualTo(86);
        assertThat(res.get(3).getLikeCount()).isEqualTo(12);
    }

    @Test
    void searchLessonLogin() throws UserNotFoundException {
        assertThat(lessonService.searchLessonLogin(user, 0, "제", 1).size()).isEqualTo(1);
        assertThat(lessonService.searchLessonLogin(user, 0, "재", 1).size()).isEqualTo(0);
        assertThat(lessonService.searchLessonLogin(user, 1, "노호준", 1).size()).isEqualTo(1);
        assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> lessonService.searchLessonLogin(user, 1, "김호준", 1));
    }

    @Test
    void searchLesson() throws UserNotFoundException {
        assertThat(lessonService.searchLesson(0, "제", 1).size()).isEqualTo(1);
        assertThat(lessonService.searchLesson(0, "재", 1).size()).isEqualTo(0);
        assertThat(lessonService.searchLesson(1, "노호준", 1).size()).isEqualTo(1);
        assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> lessonService.searchLesson(1, "김호준", 1));
    }

    @Test
    void updateLesson() throws UserNotMatchingException, LessonNotFoundException, IOException, NoSuchAlgorithmException {
        UpdateLessonReq req = new UpdateLessonReq();
        req.setTitle("바꿀 제목");
        req.setRoomType(RoomType.PRIVATE);
        Lesson updatedLesson = lessonService.updateLesson(user, lesson.getLessonId(), req, null);
        assertThat(updatedLesson.getTitle()).isEqualTo("바꿀 제목");
        assertThat(updatedLesson.getRoomType()).isEqualTo(RoomType.PRIVATE);
    }

    @Test
    void deleteLesson() throws UserNotMatchingException, LessonNotFoundException {
        lessonService.deleteLesson(user, lesson.getLessonId());
        assertThat(lessonService.getLessonList(1).size()).isEqualTo(0);
    }

    @Test
    void likeLesson() throws LessonNotFoundException {
        lessonService.likeLesson(user, lesson.getLessonId());
        assertThat(lesson.getLikeCount()).isEqualTo(1);
        assertThat(userLessonLikeRepository.findByUserAndLesson(user, lesson).isPresent()).isTrue();
    }

    @Test
    void cancelLikeLesson() throws LessonNotFoundException {
        lessonService.likeLesson(user, lesson.getLessonId());
        lessonService.cancelLikeLesson(user, lesson.getLessonId());
        assertThat(lesson.getLikeCount()).isEqualTo(0);
        assertThat(userLessonLikeRepository.findByUserAndLesson(user, lesson).isEmpty()).isTrue();
    }
}