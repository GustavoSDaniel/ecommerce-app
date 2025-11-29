package com.gustavosdaniel.ecommerce_api.config;

import com.gustavosdaniel.ecommerce_api.user.JWTUser;
import com.gustavosdaniel.ecommerce_api.user.UserNotAuthorizationException;
import com.gustavosdaniel.ecommerce_api.user.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Component
public class UserAuthorizationRole {

    private final static Logger log = LoggerFactory.getLogger(UserAuthorizationRole.class);

    public void validateUserRole(UUID userId, Authentication authorization) {

        JWTUser jwtUser = (JWTUser) authorization.getPrincipal();

        UserRole role = UserRole.valueOf(jwtUser.role());

        if (role == UserRole.ADMIN) {
            log.debug("Acessp liberado: Usuário é ADMIN");
            return;
        }

        if (role == UserRole.CUSTOMER && !jwtUser.UserId().equals(userId)) {

            log.debug("Acesso negado: Usuário {} tento acessar recursos de ADMIN", userId);

            throw new UserNotAuthorizationException();
        }
    }
}
