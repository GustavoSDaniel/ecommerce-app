package com.gustavosdaniel.ecommerce_api.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toUserRegister(UserRegisterRequest userRegisterRequest) {

        if (userRegisterRequest == null) {
            return null;
        }

        return new User(
                userRegisterRequest.userName(),
                passwordEncoder.encode(userRegisterRequest.password()),
                userRegisterRequest.email()
        );
    }

    public UserRegisterResponse toUserRegisterResponse(User user) {

        if (user == null) {
            return null;
        }

        return new UserRegisterResponse(

                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getUserRole()

        );
    }
}
