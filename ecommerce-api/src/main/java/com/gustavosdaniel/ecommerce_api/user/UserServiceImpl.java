package com.gustavosdaniel.ecommerce_api.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    private UserRole getUserRole() {

        boolean hasAnyUser = userRepository.existsByUserRole(UserRole.ADMIN);

        return hasAnyUser ? UserRole.CUSTOMER : UserRole.ADMIN;
    }
}
