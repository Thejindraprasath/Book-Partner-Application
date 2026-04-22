package com.sprint.Book_Partner_Application.security;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.Book_Partner_Application.security.dto.AuthUserResponse;

@RestController
public class AuthController {

    @GetMapping("/auth/me")
    public AuthUserResponse getCurrentUser(Authentication authentication) {
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new AuthUserResponse(authentication.getName(), roles, true);
    }
}
