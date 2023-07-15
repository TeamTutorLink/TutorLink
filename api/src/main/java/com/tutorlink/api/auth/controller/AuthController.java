package com.tutorlink.api.auth.controller;

import com.tutorlink.api.auth.dto.KakaoTokenInfo;
import com.tutorlink.api.auth.dto.KakaoUserInfo;
import com.tutorlink.api.auth.dto.request.KakaoLoginReq;
import com.tutorlink.api.auth.dto.response.LoginRes;
import com.tutorlink.api.auth.jwt.JwtTokenProvider;
import com.tutorlink.api.auth.service.KakaoAuthService;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.repository.UserRepository;
import com.tutorlink.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final KakaoAuthService kakaoAuthService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/kakao-login")
    public ResponseEntity<Object> kakaoLogin(@RequestBody @Valid KakaoLoginReq req) throws Exception {
        String code = req.getCode();

        KakaoTokenInfo kakaoTokenInfo = kakaoAuthService.sendCode(code);
        KakaoUserInfo kakaoUserInfo = kakaoAuthService.sendToken(kakaoTokenInfo.getAccessToken());

        User user = userService.getUserByKakaoSocialId(kakaoUserInfo.getId());

        if (user == null) {
            log.info("가입된 회원이 존재하지 않습니다.");
            user = userService.addUser(kakaoUserInfo);
            log.info("가입 완료");
        } else {
            log.info("가입된 회원이 존재합니다.");
        }
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        LoginRes res = new LoginRes(accessToken, refreshToken);

        return ResponseEntity.ok(res);
    }
}
