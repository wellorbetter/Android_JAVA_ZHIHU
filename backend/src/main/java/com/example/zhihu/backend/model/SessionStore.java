package com.example.zhihu.backend.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionStore {
    private final Map<String, String> tokenToUser = new ConcurrentHashMap<>();

    public String createToken(String mobile) {
        String token = "token_" + mobile + "_" + System.currentTimeMillis();
        tokenToUser.put(token, mobile);
        return token;
    }

    public boolean isValid(String token) {
        return token != null && tokenToUser.containsKey(token);
    }

    public String userOf(String token) {
        return tokenToUser.get(token);
    }

    public void revoke(String token) {
        if (token != null) {
            tokenToUser.remove(token);
        }
    }
}
