package com.example.android_java.Feature.Auth;

public final class AuthDtos {

    private AuthDtos() {
    }

    public static class LoginRequest {
        public String mobile;
        public String password;
    }

    public static class LoginData {
        public String accessToken;
        public String refreshToken;
        public long expireAt;
    }
}
