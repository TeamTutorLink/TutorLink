package com.tutorlink.api.lesson.domain;

import com.tutorlink.api.lesson.enumeration.RoomType;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    int lessonId;

    @NonNull
    @Column(name = "title", nullable = false, length = 50)
    String title;

    @Column(name = "password")
    String password;

    @NonNull
    @Column(name = "room_type", nullable = false)
    @Enumerated(EnumType.STRING)
    RoomType roomType;

    @NonNull
    @Column(name = "like_count", nullable = false)
    int likeCount;

    @Column(name = "image")
    String image;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    Date createTime;

//    @NonNull
//    @Column(name = "user_id", nullable = false)
    @Transient
    int userId;
}
