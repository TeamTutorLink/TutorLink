package com.tutorlink.api.auth.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginRes {
    String accessToken;
    String refreshToken;
}
