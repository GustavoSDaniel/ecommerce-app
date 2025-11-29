package com.gustavosdaniel.ecommerce_api.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
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

            Pageable pageable = Pageable.unpaged();

            User user = new User("Gustavo", "senha123", "gustavosdaniel.com");
            User user1 = new User("Usuario1", "senha123", "gustavosdaniel1.com");
            User user2 = new User("Usuario2", "senha123", "gustavosdaniel2.com");

            List<User> allUsers = Arrays.asList(user, user1, user2);

            Page<User> allUserPage = new PageImpl<>(allUsers, pageable, allUsers.size());

            UserResponse userResponse = new UserResponse(
                    UUID.randomUUID(), "Gustavo", "gustavosdaniel.com", UserRole.ADMIN);

            UserResponse userResponse1 = new UserResponse(
                    UUID.randomUUID(), "Gustavo", "gustavosdaniel1.com", UserRole.CUSTOMER);

            UserResponse userResponse2 = new UserResponse(
                    UUID.randomUUID(), "Gustavo", "gustavosdaniel2.com", UserRole.CUSTOMER);

            when(userRepository.findAll(pageable)).thenReturn(allUserPage);

            when(userMapper.toUserResponse(user)).thenReturn(userResponse);
            when(userMapper.toUserResponse(user1)).thenReturn(userResponse1);
            when(userMapper.toUserResponse(user2)).thenReturn(userResponse2);

            Page<UserResponse> output = userService.getUsers(pageable);

            assertNotNull(output);

            verify(userRepository).findAll(pageable);
            verify(userMapper, times(3)).toUserResponse(any(User.class));




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