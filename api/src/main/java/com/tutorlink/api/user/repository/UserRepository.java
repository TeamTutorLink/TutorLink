package com.tutorlink.api.user.repository;

import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.enumeration.SocialLoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findBySocialLoginTypeAndSocialId(SocialLoginType socialLoginType, String socialId);
}
