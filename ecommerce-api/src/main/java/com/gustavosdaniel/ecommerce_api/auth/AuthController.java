package com.gustavosdaniel.ecommerce_api.auth;

import com.gustavosdaniel.ecommerce_api.config.TokenConfig;
import com.gustavosdaniel.ecommerce_api.user.User;
import com.gustavosdaniel.ecommerce_api.user.UserRegisterRequest;
import com.gustavosdaniel.ecommerce_api.user.UserRegisterResponse;
import com.gustavosdaniel.ecommerce_api.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrando usuário")
    public ResponseEntity<UserRegisterResponse> register(
            @RequestBody @Valid UserRegisterRequest userRegisterRequest
    ){

        UserRegisterResponse newUser = userService.register(userRegisterRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build().toUri();

        return ResponseEntity.created(location).body(newUser);
    }

    @PostMapping("/login")
    @Operation(summary = "Fazendo login do usuário")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest){

        UsernamePasswordAuthenticationToken userAndPassword =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(), loginRequest.password()
                );

        Authentication authentication = authenticationManager.authenticate(userAndPassword);

        User user = (User) authentication.getPrincipal();

        String token = tokenConfig.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}

