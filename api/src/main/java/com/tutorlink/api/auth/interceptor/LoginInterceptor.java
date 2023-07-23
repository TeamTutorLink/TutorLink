package com.tutorlink.api.auth.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorlink.api.auth.annotation.LoginRequired;
import com.tutorlink.api.auth.dto.response.LoginRes;
import com.tutorlink.api.auth.jwt.JwtTokenProvider;
import com.tutorlink.api.common.error.ErrorResponse;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);
        if (Objects.isNull(loginRequired)) {
            return true;
        }

        String accessToken = request.getHeader("accessToken");
        String refreshToken = request.getHeader("refreshToken");

        ErrorResponse errorResponse = null;

        if (accessToken != null) {
            try {
                jwtTokenProvider.validateToken(accessToken);
                int userId = jwtTokenProvider.getUserId(accessToken);
                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isEmpty()) {
                    errorResponse = new ErrorResponse("400", "사용자가 존재하지 않습니다");
                    response.setStatus(400);
                } else {
                    request.setAttribute("user", userOpt.get());
                    return true;
                }
            } catch (ExpiredJwtException e) {
                errorResponse = new ErrorResponse("401", "만료된 액세스 토큰");
                response.setStatus(401);
            } catch (Exception e) {
                errorResponse = new ErrorResponse("401", "유효하지 않은 액세스 토큰");
                response.setStatus(401);
            }
        } else if (refreshToken != null) {
            try {
                jwtTokenProvider.validateToken(refreshToken);
                int userId = jwtTokenProvider.getUserId(refreshToken);
                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isEmpty()) {
                    errorResponse = new ErrorResponse("400", "사용자가 존재하지 않습니다");
                    response.setStatus(400);
                } else {
                    if (refreshToken.equals(userOpt.get().getRefreshToken())) {
                        String newAccessToken = jwtTokenProvider.createAccessToken(userId);
                        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);

                        userOpt.get().setRefreshToken(newRefreshToken);
                        userRepository.save(userOpt.get());

                        LoginRes res = new LoginRes(newAccessToken, newRefreshToken);

                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");

                        ObjectMapper objectMapper = new ObjectMapper();
                        response.getWriter().write(objectMapper.writeValueAsString(res));

                        return false;
                    } else {
                        errorResponse = new ErrorResponse("401", "유효하지 않은 리프레쉬 토큰");
                        response.setStatus(401);
                    }
                }
            } catch (ExpiredJwtException e) {
                errorResponse = new ErrorResponse("401", "만료된 리프레쉬 토큰");
                response.setStatus(401);
            } catch (Exception e) {
                errorResponse = new ErrorResponse("401", "유효하지 않은 리프레쉬 토큰");
                response.setStatus(401);
            }
        } else {
            errorResponse = new ErrorResponse("401", "토큰이 존재하지 않습니다");
            response.setStatus(401);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

        return false;
    }
}
