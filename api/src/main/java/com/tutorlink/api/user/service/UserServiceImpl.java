package com.tutorlink.api.user.service;

import com.tutorlink.api.auth.dto.KakaoUserInfo;
import com.tutorlink.api.common.domain.ImageFile;
import com.tutorlink.api.common.repository.ImageFileRepository;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.dto.request.UpdateUserReq;
import com.tutorlink.api.user.enumeration.SocialLoginType;
import com.tutorlink.api.user.enumeration.UserType;
import com.tutorlink.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ImageFileRepository imageFileRepository;

    @Value("${user.image.file.dir}")
    private String userImageFileDir;

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

    @Override
    public void changeToTeacher(User user) {
        user.setUserType(UserType.TEACHER);
        userRepository.save(user);
    }

    @Override
    public User updateUser(User user, UpdateUserReq req, MultipartFile imageFile) throws IOException {
        BeanUtils.copyProperties(req, user);
        userRepository.save(user);
        if (imageFile != null) {
            String uploadFileName = imageFile.getOriginalFilename();
            String ext = extractExt(uploadFileName);
            String storeFileName = UUID.randomUUID().toString() + "." + ext;

            imageFileRepository.save(new ImageFile(storeFileName, uploadFileName, ext, new Date()));

            String fullPath = userImageFileDir + storeFileName;
            imageFile.transferTo(new File(fullPath));

            user.setImage(storeFileName);
        }
        return user;
    }

    @Override
    public void logout(User user) {
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public String extractExt(String uploadFileName) {
        int index = uploadFileName.lastIndexOf('.');
        String ext = null;
        if (index != -1) {
            ext = uploadFileName.substring(index + 1);
        }
        return ext;
    }
}
