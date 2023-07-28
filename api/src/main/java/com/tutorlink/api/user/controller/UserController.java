package com.tutorlink.api.user.controller;

import com.tutorlink.api.auth.annotation.LoginRequired;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.dto.request.UpdateUserReq;
import com.tutorlink.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/change-to-teacher")
    @LoginRequired
    public ResponseEntity<Object> changeToTeacher(HttpServletRequest servletRequest) {

        User user = (User) servletRequest.getAttribute("user");
        userService.changeToTeacher(user);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    @LoginRequired
    public ResponseEntity<Object> updateUser(HttpServletRequest servletRequest,
                                             @RequestPart @Valid UpdateUserReq req,
                                             @RequestPart @Nullable MultipartFile imageFile) throws IOException {

        User user = (User) servletRequest.getAttribute("user");
        userService.updateUser(user, req, imageFile);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    @LoginRequired
    public ResponseEntity<Object> logout(HttpServletRequest servletRequest) {

        User user = (User) servletRequest.getAttribute("user");
        userService.logout(user);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @LoginRequired
    public ResponseEntity<Object> deleteUser(HttpServletRequest servletRequest) {

        User user = (User) servletRequest.getAttribute("user");
        userService.deleteUser(user);

        return ResponseEntity.ok().build();
    }
}
