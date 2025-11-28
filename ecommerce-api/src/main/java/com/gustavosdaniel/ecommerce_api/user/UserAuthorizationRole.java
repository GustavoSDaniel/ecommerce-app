package com.gustavosdaniel.ecommerce_api.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserAuthorizationRole {

    private final static Logger log = LoggerFactory.getLogger(UserAuthorizationRole.class);

    public void validateUserRole(UUID userId, Authentication authorization) {

        JWTUser jwtUser = (JWTUser) authorization.getPrincipal();

        UserRole role = UserRole.valueOf(jwtUser.role());

        if (role == UserRole.ADMIN) {
            log.debug("Admin role valido");
            return;
        }

        if (role == UserRole.CUSTOMER && !jwtUser.UserId().equals(userId)) {
            log.debug("Customer role valido");
            throw new UserNotAuthorizationException();
        }
    }
}
