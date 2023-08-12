package com.tutorlink.api.lesson.domain;

import com.tutorlink.api.lesson.enumeration.RoomType;
import com.tutorlink.api.user.domain.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "user_id"),
        @Index(columnList = "like_count")
})
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

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.REMOVE)
    List<UserLessonLike> userLessonLikes = new ArrayList<>();
}
