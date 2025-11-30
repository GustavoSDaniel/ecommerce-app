package com.gustavosdaniel.ecommerce_api.user;

import com.gustavosdaniel.ecommerce_api.config.UserAuthorizationRole;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Email;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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

    @GetMapping
    @Operation(summary = "Obter todos os usuários")
    public ResponseEntity<Page<UserResponse>>  getAllUsers(
            @ParameterObject
            @PageableDefault(size = 20, sort = "userName", direction = Sort.Direction.ASC)
            Pageable pageable) {

        Page<UserResponse> allUsers = userService.getUsers(pageable);

        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/email")
    @Operation(summary = "Busca usuário pelo email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam @Email String email) {

        Optional<UserResponse> user = userService.getUserByEmail(email);

        return user
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/searchUsers")
    @Operation(summary = "Buscando usuários pelo filtro de busca")
    public ResponseEntity<Page<UserResponse>> searchUsers(

            @RequestParam(required = false) String userName,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String phoneNumber,
            @PageableDefault(size = 20, sort = "userName", direction = Sort.Direction.ASC)
            Pageable pageable) {

        Page<UserResponse> users = userService.searchUsers(userName, role, cpf, phoneNumber, pageable);

        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta usuário")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id, Authentication authentication) {

        authorizationRole.validateUserRole(id, authentication);

        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
}
