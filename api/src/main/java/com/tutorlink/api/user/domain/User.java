package com.tutorlink.api.user.domain;

import com.tutorlink.api.lesson.domain.Lesson;
import com.tutorlink.api.lesson.domain.UserLessonLike;
import com.tutorlink.api.user.enumeration.SocialLoginType;
import com.tutorlink.api.user.enumeration.UserType;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    int userId;

    @NonNull
    @Column(name = "social_login_type", nullable = false)
    @Enumerated(EnumType.STRING)
    SocialLoginType socialLoginType;

    @NonNull
    @Column(name = "social_id", nullable = false)
    String socialId;

    @NonNull
    @Column(name = "user_name", nullable = false, length = 50)
    String userName;

    @Column(name = "email", length = 50)
    String email;

    @Column(name = "introduction")
    String introduction;

    @Column(name = "image")
    String image;

    @NonNull
    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    UserType userType;

    @Column(name = "refresh_token")
    String refreshToken;

    @NonNull
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    Date createTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<UserLessonLike> userLessonLikes = new ArrayList<>();
}
