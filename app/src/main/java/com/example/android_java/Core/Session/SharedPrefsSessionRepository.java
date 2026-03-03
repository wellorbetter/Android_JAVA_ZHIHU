package com.example.android_java.Core.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsSessionRepository implements SessionRepository {

    private static final String PREF_NAME = "zhihu_clone_pref";
    private static final String KEY_PRIVATE_MODE = "private_mode";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    private final SharedPreferences sharedPreferences;

    public SharedPrefsSessionRepository(Context context) {
        this.sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isPrivateModeEnabled() {
        return sharedPreferences.getBoolean(KEY_PRIVATE_MODE, true);
    }

    @Override
    public void setPrivateModeEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_PRIVATE_MODE, enabled).apply();
    }

    @Override
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false);
    }

    @Override
    public void setLoggedIn(boolean loggedIn) {
        sharedPreferences.edit().putBoolean(KEY_LOGGED_IN, loggedIn).apply();
    }

    @Override
    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, "");
    }

    @Override
    public void setAccessToken(String accessToken) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, accessToken == null ? "" : accessToken).apply();
    }

    @Override
    public void clearSession() {
        sharedPreferences.edit()
                .putBoolean(KEY_LOGGED_IN, false)
                .putString(KEY_ACCESS_TOKEN, "")
                .apply();
    }
}
