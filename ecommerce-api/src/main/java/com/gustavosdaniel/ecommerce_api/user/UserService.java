package com.gustavosdaniel.ecommerce_api.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserRegisterResponse register(UserRegisterRequest user);

    Page<UserResponse> getUsers(Pageable pageable);

    Optional<UserResponse> getUserByEmail(String email);

    void deleteUserById(UUID id);
}
