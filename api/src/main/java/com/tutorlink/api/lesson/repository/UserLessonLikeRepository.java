package com.tutorlink.api.lesson.repository;

import com.tutorlink.api.lesson.domain.Lesson;
import com.tutorlink.api.lesson.domain.UserLessonLike;
import com.tutorlink.api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLessonLikeRepository extends JpaRepository<UserLessonLike, Integer> {

    List<UserLessonLike> findAllByUserAndLessonIn(User user, List<Lesson> lessonList);

    Optional<UserLessonLike> findByUserAndLesson(User user, Lesson lesson);

    void deleteByUserAndLesson(User user, Lesson lesson);
}
