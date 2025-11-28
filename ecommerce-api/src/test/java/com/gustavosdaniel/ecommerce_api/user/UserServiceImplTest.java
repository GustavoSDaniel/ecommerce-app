package com.gustavosdaniel.ecommerce_api.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    // Arrange (preparação), act (ação), assert (Verificação)

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    class registerUser {

        @Test
        @DisplayName("Should create a user with success")
        void shoulRegisterUser() {

            UUID uuid = UUID.randomUUID();
            String password = "senha123";
            String HASHED_PASSWORD = "passworhashed";
            String email = "gustavosdaniel@hotmail.com";

            UserRegisterRequest requeste = new UserRegisterRequest("Gustavp",
                    password,
                    email);

            User savedUser = new User("Gustavo", HASHED_PASSWORD, email);
            savedUser.setUserRole(UserRole.ADMIN);
            ReflectionTestUtils.setField(savedUser, "id", uuid);

            when(userMapper.toUserRegister(requeste)).thenReturn(savedUser);
            when(userMapper.toUserRegisterResponse(savedUser)).
                    thenReturn(new UserRegisterResponse(uuid, "Gustavo", email, UserRole.ADMIN));
            when(userRepository.existsByEmail(email)).thenReturn(false);
            when(userRepository.existsByUserRole(UserRole.ADMIN)).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(savedUser);

            UserRegisterResponse output = userService.register(requeste);

            assertNotNull(output.userId());
            assertEquals(email, output.email());
            assertEquals(UserRole.ADMIN, output.role());

            verify(userRepository).save(any(User.class));

        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailExists() {

            String email = "gustavosdaniel@hotmail.com";
            UserRegisterRequest request =
                    new UserRegisterRequest("Gustavo", "senha123", email);

            when(userRepository.existsByEmail(email)).thenReturn(true);

            assertThrows(EmailUserDuplicationException.class, () -> {
                userService.register(request);
            });

            verify(userRepository, never()).save(any(User.class));

            verifyNoInteractions(userMapper);
        }

    }

    @Nested
    class allUsers {

        @Test
        @DisplayName("Should return all users")
        void shouldReturnAllUsers() {

        }
    }

    @Nested
    class deleteUser {

        @Test
        @DisplayName("Should delete user")
        void shouldDeleteUser() {

            UUID uuid = UUID.randomUUID();
            String password = "senha123";
            String email = "gustavosdanieel@hotmail.com";

            User deleteUser = new User("Gustavo", password, email);
            deleteUser.setUserRole(UserRole.ADMIN);
            ReflectionTestUtils.setField(deleteUser, "id", uuid);

            when(userRepository.findById(uuid)).thenReturn(Optional.of(deleteUser));

            userService.deleteUserById(uuid);

            verify(userRepository).findById(uuid);
            verify(userRepository).delete(deleteUser);

        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {

            UUID uuid = UUID.randomUUID();

            when(userRepository.findById(uuid)).thenReturn(Optional.empty());
            assertThrows(UserNotFoundException.class, () -> {
                userService.deleteUserById(uuid);
            });

            verify(userRepository).findById(uuid);
        }
    }

}