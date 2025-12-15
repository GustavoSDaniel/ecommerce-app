package com.gustavosdaniel.ecommerce_api.user;

import com.gustavosdaniel.ecommerce_api.address.Address;
import com.gustavosdaniel.ecommerce_api.address.AddressMapper;
import com.gustavosdaniel.ecommerce_api.notification.NotificationServiceImpl;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final NotificationServiceImpl notificationService;
    private static final  Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, AddressMapper addressMapper, NotificationServiceImpl notificationService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.addressMapper = addressMapper;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
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

        notificationService.notifyUserRegistration(userRegister);

        return userMapper.toUserRegisterResponse(userRegister);
    }

    @Override
    @Cacheable(key = "'all-users-page-' + #pageable.pageNumber + '-size-' + #pageable.pageSize")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
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
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Optional<UserResponse> getUserByEmail(String email) {

        log.info("Iniciando busca de usuário pelo email: {}", email);

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            log.warn("Nenhum usuário encontrado para o email: {}", email);
        }

        return user.map(userMapper::toUserResponse);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
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
    @CacheEvict(allEntries = true)
    public UserUpdateResponse updateUser(UUID id, UserUpdateRequest request) {

        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        log.info("Atualizando usuário");

        if (request.email() != null && !request.email().isBlank()) {

            if (!request.email().equals(user.getEmail()) &&
                    userRepository.existsByEmail(request.email())) {

                throw new EmailUserDuplicationException();
            }
        }

       if (request.role() != null && request.role().equals(user.getUserRole())) {

           log.info("Usuário já possui a role {}", user.getUserRole());       }

        userMapper.updateUser(request, user);

       if (request.address() != null) {

           if (user.getAddresses() == null) {

               user.setAddresses(new ArrayList<>());
           }

           Address newAddress = addressMapper.toAddress(request.address());

           newAddress.setUser(user);

           user.getAddresses().add(newAddress);

           log.info("Adicionando um novo endereço para o Usuário");

       }

        User savedUser = userRepository.save(user);

       log.info("Usuário {} atualizado com sucesso ", savedUser.getUserName());

        return userMapper.toUserUpdate(savedUser);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public UserCpfResponse addCpfToUser(UUID id, UserAddCpf userAddCpf) {

        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        log.info("Adicionando Cpf para o usuário: {}", user.getUserName());


        if (user.getCpf() != null && !user.getCpf().isBlank()) {

            log.warn("Usuário já possui CPF cadastrado");

            throw new CpfAlreadyRegisterException();
        }

        if (userAddCpf.cpf() != null && !userAddCpf.cpf().isBlank()) {

            boolean existCpf = userRepository.existsByCpf(userAddCpf.cpf());

            if (existCpf) {

                throw new CpfExistsException();

            }

            user.setCpf(userAddCpf.cpf());
        }

        User savedUser = userRepository.save(user);

        log.info("Cpf adicionado com sucesso");

        return userMapper.toUserCpf(savedUser);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
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
