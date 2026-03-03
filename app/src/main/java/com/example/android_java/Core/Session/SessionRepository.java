package com.example.android_java.Core.Session;

public interface SessionRepository {
    boolean isPrivateModeEnabled();

    void setPrivateModeEnabled(boolean enabled);

    boolean isLoggedIn();

    void setLoggedIn(boolean loggedIn);

    String getAccessToken();

    void setAccessToken(String accessToken);

    void clearSession();
}
