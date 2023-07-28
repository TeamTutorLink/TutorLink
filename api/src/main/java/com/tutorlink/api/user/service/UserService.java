package com.tutorlink.api.user.service;

import com.tutorlink.api.auth.dto.KakaoUserInfo;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.dto.request.UpdateUserReq;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    User addUser(KakaoUserInfo kakaoUserInfo);

    User getUserByKakaoSocialId(String kakaoSocialId);

    void changeToTeacher(User user);

    User updateUser(User user, UpdateUserReq req, MultipartFile imagefile) throws IOException;

    void logout(User user);

    void deleteUser(User user);
}
