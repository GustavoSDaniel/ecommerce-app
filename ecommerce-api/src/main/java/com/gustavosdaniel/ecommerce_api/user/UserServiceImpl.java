package com.gustavosdaniel.ecommerce_api.user;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
    @Transactional
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
    public Page<UserResponse> getUsers(Pageable pageable) {

        log.info("Buscando todos os usuários");

        Page<User> users = userRepository.findAll(pageable);

        if (users.isEmpty()) {
            return Page.empty();
        }

        log.info("Retornou users {}", users.getNumberOfElements());

        return users.map(userMapper::toUserResponse);
    }

    @Override
    public Optional<UserResponse> getUserByEmail(String email) {

        log.info("Iniciando busca de usuário pelo email: {}", email);

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            log.warn("Nenhum usuário encontrado para o email: {}", email);
        }

        return user.map(userMapper::toUserResponse);
    }

    @Override
    public Page<UserResponse> searchUsers(
            String userName, UserRole role, String cpf, String phoneNumber, Pageable pageable) {

        log.info("Buscando usuários com filtros aplicados");

        Specification<User> specification = UserSpecification.filter(userName, role, cpf, phoneNumber);

        Page<User> users = userRepository.findAll(specification, pageable);

        log.info("Busca finalizada retornou os users {}", users.getNumberOfElements());

        return users.map(userMapper::toUserResponse);
    }

    @Override
    @Transactional
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
