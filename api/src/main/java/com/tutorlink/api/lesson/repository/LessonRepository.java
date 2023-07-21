package com.tutorlink.api.lesson.repository;

import com.tutorlink.api.lesson.domain.Lesson;
import com.tutorlink.api.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    Page<Lesson> findByTitleContaining(String keyword, Pageable pageable);

    Page<Lesson> findByUser(User user, Pageable pageable);
}
