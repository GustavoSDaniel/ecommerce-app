package com.gustavosdaniel.ecommerce_api.user;

import com.gustavosdaniel.ecommerce_api.address.AddressMapper;
import com.gustavosdaniel.ecommerce_api.address.AddressResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    private final AddressMapper addressMapper;
    private final PasswordEncoder passwordEncoder;

    public UserMapper(AddressMapper addressMapper, PasswordEncoder passwordEncoder) {
        this.addressMapper = addressMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User toUserRegister(UserRegisterRequest userRegisterRequest) {

        if (userRegisterRequest == null) {
            return null;
        }

        return new User(
                userRegisterRequest.userName(),
                passwordEncoder.encode(userRegisterRequest.password()),
                userRegisterRequest.email()
        );
    }

    public UserRegisterResponse toUserRegisterResponse(User user) {

        if (user == null) {
            return null;
        }

        return new UserRegisterResponse(

                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getUserRole()

        );
    }

    public UserResponse toUserResponse(User user) {

        if (user == null) {
            return null;
        }

        return new UserResponse(

                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getUserRole()

        );
    }

    public void updateUser(UserUpdateRequest userUpdateRequest, User user) {

        if (userUpdateRequest == null) {
            return;
        }

        if (userUpdateRequest.userName() != null && !userUpdateRequest.userName().isBlank()) {

            user.setUserName(userUpdateRequest.userName());
        }

        if (userUpdateRequest.password() != null && !userUpdateRequest.password().isBlank()) {

            user.setPassword(passwordEncoder.encode(userUpdateRequest.password()));
        }

        if (userUpdateRequest.email() != null && !userUpdateRequest.email().isBlank()) {

            user.setEmail(userUpdateRequest.email());
        }

        if (userUpdateRequest.role() != null){

            user.setUserRole(userUpdateRequest.role());
        }

        if (userUpdateRequest.phoneNumber() != null && !userUpdateRequest.phoneNumber().isBlank()) {

            user.setPhoneNumber(userUpdateRequest.phoneNumber());
        }
    }

    public UserUpdateResponse toUserUpdate(User user) {
        if (user == null) {
            return null;
        }

        List<AddressResponse> addresses = user.getAddresses()
                .stream()
                .map(addressMapper::toAddressResponse)
                .toList();

        return new UserUpdateResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getUserRole(),
                user.getPhoneNumber(),
                addresses
        );
    }

    public UserCpfResponse toUserCpf (User user) {

        if (user == null) {
            return null;
        }

        return new UserCpfResponse(

                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getUserRole(),
                user.getCpf()

        );
    }


}
