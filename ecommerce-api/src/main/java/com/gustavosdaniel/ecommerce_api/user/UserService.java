package com.gustavosdaniel.ecommerce_api.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    UserRegisterResponse register(UserRegisterRequest user);

    Page<UserRegisterResponse> getUsers(Pageable pageable);

    void deleteUserById(UUID id);
}
