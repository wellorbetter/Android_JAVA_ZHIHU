package com.example.zhihu.backend.service;

import com.example.zhihu.backend.model.SessionStore;

public class AuthService {
    private final SessionStore sessionStore;

    public AuthService(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public String login(String mobile) {
        String normalized = (mobile == null || mobile.trim().isEmpty()) ? "guest" : mobile.trim();
        return sessionStore.createToken(normalized);
    }

    public void logout(String token) {
        sessionStore.revoke(token);
    }

    public boolean isAuthorized(String token) {
        return sessionStore.isValid(token);
    }

    public String userOf(String token) {
        String user = sessionStore.userOf(token);
        return user == null || user.isEmpty() ? "guest" : user;
    }
}
