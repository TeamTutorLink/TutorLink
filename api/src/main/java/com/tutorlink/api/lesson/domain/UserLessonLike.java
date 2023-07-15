package com.tutorlink.api.lesson.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "user_lesson_like")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
public class UserLessonLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_lesson_like_id")
    int userLessonLikeId;

    @NonNull
    @Column(name = "user_id", nullable = false)
    int userId;

    @NonNull
    @Column(name = "lesson_id", nullable = false)
    int lessonId;
}
