package com.tutorlink.api.user.service;

import com.tutorlink.api.auth.dto.KakaoUserInfo;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.dto.request.UpdateUserReq;
import com.tutorlink.api.user.enumeration.SocialLoginType;
import com.tutorlink.api.user.enumeration.UserType;
import com.tutorlink.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    private User user = null;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @BeforeEach
    void clearRepository() {
        userRepository.deleteAll();

        User newUser = new User(SocialLoginType.KAKAO, "23712326", "노호준", UserType.STUDENT, new Date());
        User saveUser = userRepository.save(newUser);
        user = saveUser;
    }

    @Test
    void addUser() {
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo();
        kakaoUserInfo.setId("47473828");
        kakaoUserInfo.setName("김호준");
        kakaoUserInfo.setEmail("sdfjdjf@asdasd.com");

        User saveUser = userService.addUser(kakaoUserInfo);

        assertThat(userRepository.findById(saveUser.getUserId()).isPresent()).isTrue();
    }

    @Test
    void getUserByKakaoSocialId() {
        assertThat(userService.getUserByKakaoSocialId(user.getSocialId())).isNotNull();
    }

    @Test
    void changeToTeacher() {
        userService.changeToTeacher(user);

        assertThat(userRepository.findById(user.getUserId()).get().getUserType()).isEqualTo(UserType.TEACHER);
    }

    @Test
    void updateUser() throws IOException {
        UpdateUserReq req = new UpdateUserReq();
        req.setUserName("ㅋㅋㅋ");
        req.setIntroduction("안녕하세요");

        userService.updateUser(user, req, null);

        assertThat(user.getUserName()).isEqualTo("ㅋㅋㅋ");
        assertThat(user.getIntroduction()).isEqualTo("안녕하세요");
    }

    @Test
    void logout() {
        userService.logout(user);

        assertThat(userRepository.findById(user.getUserId()).get().getRefreshToken()).isNull();
    }

    @Test
    void deleteUser() {
        userService.deleteUser(user);

        assertThat(userRepository.findById(user.getUserId()).isEmpty()).isTrue();
    }
}