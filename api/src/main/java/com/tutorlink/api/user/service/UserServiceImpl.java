package com.tutorlink.api.user.service;

import com.tutorlink.api.auth.dto.KakaoUserInfo;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.enumeration.SocialLoginType;
import com.tutorlink.api.user.enumeration.UserType;
import com.tutorlink.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(KakaoUserInfo kakaoUserInfo) {
        User user = new User(SocialLoginType.KAKAO, kakaoUserInfo.getId(), kakaoUserInfo.getName(), UserType.STUDENT, new Date());
        if (kakaoUserInfo.getEmail() != null) {
            user.setEmail(kakaoUserInfo.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public User getUserByKakaoSocialId(String kakaoSocialId) {
        return userRepository.findBySocialLoginTypeAndSocialId(SocialLoginType.KAKAO, kakaoSocialId);
    }
}
