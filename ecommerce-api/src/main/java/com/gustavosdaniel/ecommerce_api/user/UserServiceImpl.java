package com.gustavosdaniel.ecommerce_api.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserRegisterResponse register(UserRegisterRequest user) {

        log.info("Register user {}", user.userName());

        if (userRepository.existsByEmail(user.email())){

            log.error("Usuário com esse email {} já existe", user.email());

            throw new EmailUserDuplicationException();
        }

        User newUser = userMapper.toUserRegister(user);
        newUser.setUserRole(getUserRole());

        User userRegister = userRepository.save(newUser);

        log.info("Saved user {}", userRegister);

        return userMapper.toUserRegisterResponse(userRegister);
    }

    @Override
    public Page<UserRegisterResponse> getUsers(Pageable pageable) {

        log.info("Get users {}", pageable);

        Page<User> users = userRepository.findAll(pageable);

        if (users.isEmpty()) {
            return Page.empty();
        }

        log.info("Retornou users {}", users);

        return users.map(userMapper::toUserRegisterResponse);
    }

    @Override
    public void deleteUserById(UUID id) {

        log.info("Delete user {}", id);

        User deletedUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        userRepository.delete(deletedUser);

        log.info("Deleted user sucesso {}", deletedUser.getUserName());

    }


    private UserRole getUserRole() {

        boolean hasAnyUser = userRepository.existsByUserRole(UserRole.ADMIN);

        return hasAnyUser ? UserRole.CUSTOMER : UserRole.ADMIN;
    }
}
