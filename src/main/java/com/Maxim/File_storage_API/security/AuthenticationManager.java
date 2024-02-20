package com.Maxim.File_storage_API.security;


import com.Maxim.File_storage_API.service.UserService;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    public AuthenticationManager(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userService.findUserById(principal.getId())
//                .filter(UserEntity::isEnabled)
                .switchIfEmpty(Mono.error(new RuntimeException("User disabled")))
                .map(user -> authentication);
    }
}