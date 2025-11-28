package com.gustavosdaniel.ecommerce_api.user;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserAuthorizationRole authorizationRole;

    public UserController(UserService userService, UserAuthorizationRole authorizationRole) {
        this.userService = userService;
        this.authorizationRole = authorizationRole;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletando usuário")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id, Authentication authentication) {

        authorizationRole.validateUserRole(id, authentication);

        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
}
