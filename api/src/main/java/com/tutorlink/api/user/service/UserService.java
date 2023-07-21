package com.tutorlink.api.user.service;

import com.tutorlink.api.auth.dto.KakaoUserInfo;
import com.tutorlink.api.user.domain.User;

public interface UserService {

    User addUser(KakaoUserInfo kakaoUserInfo);

    User getUserByKakaoSocialId(String kakaoSocialId);

    void changeToTeacher(User user);
}
