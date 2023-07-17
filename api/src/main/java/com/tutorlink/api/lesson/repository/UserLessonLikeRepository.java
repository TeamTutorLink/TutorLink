package com.tutorlink.api.lesson.repository;

import com.tutorlink.api.lesson.domain.UserLessonLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLessonLikeRepository extends JpaRepository<UserLessonLike, Integer> {

    List<UserLessonLike> findAllByUserIdAndLessonIdIn(int userId, List<Integer> lessonIdList);

    Optional<UserLessonLike> findByUserIdAndLessonId(int userId, int lessonId);

    void deleteByUserIdAndLessonId(int userId, int lessonId);
}
