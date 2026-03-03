package com.example.zhihu.backend.model;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionStore {
    private final Set<String> validTokens = ConcurrentHashMap.newKeySet();

    public String createToken(String mobile) {
        String token = "token_" + mobile + "_" + System.currentTimeMillis();
        validTokens.add(token);
        return token;
    }

    public boolean isValid(String token) {
        return token != null && validTokens.contains(token);
    }

    public void revoke(String token) {
        if (token != null) {
            validTokens.remove(token);
        }
    }
}
