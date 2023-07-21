package com.tutorlink.api.user.controller;

import com.tutorlink.api.auth.annotation.LoginRequired;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
}
