package com.sprint.Book_Partner_Application.security;

import java.util.ArrayList;
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

        // Store roles as String
        List<String> roles = new ArrayList<>();

        // Loop through authorities directly (no streams, no extra list)
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roles.add(authority.getAuthority());
        }

        // Return response
        return new AuthUserResponse(authentication.getName(), roles, true);
    }
}