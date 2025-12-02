package com.gustavosdaniel.ecommerce_api.user;

import com.gustavosdaniel.ecommerce_api.address.Address;
import com.gustavosdaniel.ecommerce_api.address.AddressMapper;
import com.gustavosdaniel.ecommerce_api.address.AddressRequest;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

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
    private AddressMapper addressMapper;

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

    @Nested
    class FindByEmail {

        @Test
        @DisplayName("Should return user")
        void shouldReturnUser() {

            UUID uuid = UUID.randomUUID();
            String email = "gustavosdaniel@hotmail.com";
            String password = "senha123";

            User user = new User("Gustavo", password, email);
            user.setUserRole(UserRole.CUSTOMER);
            ReflectionTestUtils.setField(user, "id", uuid);

            UserResponse userResponse = new UserResponse(
                    uuid, "Gustavo", email, UserRole.CUSTOMER);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

            when(userMapper.toUserResponse(user)).thenReturn(userResponse);

            Optional<UserResponse> output = userService.getUserByEmail(email);

            assertTrue(output.isPresent());

            assertEquals(email, output.get().email());
            assertEquals(uuid, output.get().id());

            verify(userRepository).findByEmail(email);
            verify(userMapper).toUserResponse(user);

        }
    }

    @Nested
    class SearchUser {

        @Test
        @DisplayName("Should return paginated users with filters")
        void shouldReturnUser() {

            UUID uuid = UUID.randomUUID();
            String username = "Gustavo";
            String password = "senha123";
            String email = "gustavosdaniel@hotmail.com";
            UserRole role = UserRole.CUSTOMER;
            String cpf = "123.456.789-00";
            String phoneNumber = "11999999999";

            Pageable pageable = PageRequest.of(0, 10);

            User user = new User(username, password, email);
            user.setUserRole(UserRole.CUSTOMER);
            ReflectionTestUtils.setField(user, "id", uuid);

            Page<User> userPage = new PageImpl<>(Arrays.asList(user));

            UserResponse userResponse = new UserResponse(
                    uuid, "Gustavo", email, UserRole.CUSTOMER);

            when(userRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(userPage);

            when(userMapper.toUserResponse(user)).thenReturn(userResponse);

            Page<UserResponse> output =
                    userService.searchUsers(username, role, cpf, phoneNumber, pageable);

            assertNotNull(output);
            assertEquals(1, output.getTotalElements());
            assertEquals(username, output.getContent().get(0).userName());
            assertEquals(email, output.getContent().get(0).email());
            assertEquals(role, output.getContent().get(0).role());

            verify(userRepository).findAll(any(Specification.class), eq(pageable));
            verify(userMapper).toUserResponse(user);


        }
    }

    @Nested
    class AddCpfToUser {

        @Test
        @DisplayName("Should add CPF in te user")
        void shouldReturnUser() {

            UUID uuid = UUID.randomUUID();
            String email = "gustavosdaniel@hotmail.com";
            String password = "senha123";
            String cpf = "123.456.789-00";

            User user = new User("Gustavo", password, email);
            user.setUserRole(UserRole.CUSTOMER);
            ReflectionTestUtils.setField(user, "id", uuid);

            UserAddCpf userAddCpf = new UserAddCpf(cpf);

            UserCpfResponse userResponse = new UserCpfResponse(
                    uuid, "Gustavo", email, UserRole.CUSTOMER, cpf);

            when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

            when(userRepository.existsByCpf("123.456.789-00")).thenReturn(false);

            when(userRepository.save(any(User.class))).thenReturn(user);

            when(userMapper.toUserCpf(user)).thenReturn(userResponse);

            UserCpfResponse output = userService.addCpfToUser(uuid,userAddCpf );

            assertNotNull(output);
            assertEquals(cpf, output.cpf());

            verify(userRepository).findById(uuid);
            verify(userRepository).existsByCpf("123.456.789-00");
            verify(userRepository).save(any(User.class));
            verify(userMapper).toUserCpf(user);
        }

        @Test
        @DisplayName("Should throw exception when cpf já em uso")
        void shouldThrowExceptionCpfJaEmUso() {

            UUID uuid = UUID.randomUUID();
            String email = "gustavosdaniel@hotmail.com";
            String password = "senha123";
            String cpf = "123.456.789-00";

            User user = new User("Gustavo", password, email);
            user.setUserRole(UserRole.CUSTOMER);
            ReflectionTestUtils.setField(user, "id", uuid);

            UserAddCpf userAddCpf = new UserAddCpf(cpf);

            when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
            when(userRepository.existsByCpf("123.456.789-00")).thenReturn(true);

            UserCpfResponse output = userService.addCpfToUser(uuid,userAddCpf );

            assertNotNull(output);

        }
    }

    @Nested
    class UpdateUser {

        @Test
        @DisplayName("Should update user")
        void shouldUpdateUser() {

            UUID uuid = UUID.randomUUID();
            String email = "gustavosdaniel@hotmail.com";
            String password = "senha123";

            User user = new User("Gustavo", password, email);
            user.setUserRole(UserRole.CUSTOMER);
            user.setPhoneNumber("11987654321");
            user.setAddresses(new ArrayList<>());
            ReflectionTestUtils.setField(user, "id", uuid);

            AddressRequest addressRequest = new AddressRequest(

                    "1122",
                    "Rua Teste",
                    "123",
                    "Apto 45",
                    "Centro",
                    "São Paulo",
                    "SP",
                    "01234567"
            );

            UserUpdateRequest request = new UserUpdateRequest(
                    "GustavoUpdated",
                    "newPassword123",
                    "newemail@hotmail.com",
                    UserRole.ADMIN,
                    "11999999999",
                    addressRequest
            );

            when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
            when(userRepository.existsByEmail("newemail@hotmail.com")).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(user);

            Address newAddress = new Address();

            when(addressMapper.toAddress(addressRequest)).thenReturn(newAddress);

            UserUpdateResponse expectedResponse = new UserUpdateResponse(
                    uuid,
                    "GustavoUpdated",
                    "newemail@hotmail.com",
                    UserRole.ADMIN,
                    "11999999999",
                    List.of()
            );
            when(userMapper.toUserUpdate(user)).thenReturn(expectedResponse);


            UserUpdateResponse output = userService.updateUser(uuid, request);

            assertNotNull(output);
            assertEquals("GustavoUpdated", output.userName());
            assertEquals("newemail@hotmail.com", output.email());
            assertEquals(UserRole.ADMIN, output.role());
            assertEquals("11999999999", output.phoneNumber());

            verify(userRepository).findById(uuid);
            verify(userRepository).existsByEmail("newemail@hotmail.com");
            verify(userMapper).updateUser(request, user);
            verify(addressMapper).toAddress(addressRequest);
            verify(userRepository).save(user);
            verify(userMapper).toUserUpdate(user);


        }
    }

}