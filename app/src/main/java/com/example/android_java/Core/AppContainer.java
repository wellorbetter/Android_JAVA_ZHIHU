package com.example.android_java.Core;

import android.content.Context;

import com.example.android_java.Core.Network.ApiClientProvider;
import com.example.android_java.Core.Session.SessionRepository;
import com.example.android_java.Core.Session.SharedPrefsSessionRepository;

public final class AppContainer {

    private static volatile SessionRepository sessionRepository;
    private static volatile ApiClientProvider apiClientProvider;

    private AppContainer() {
    }

    public static SessionRepository sessionRepository(Context context) {
        if (sessionRepository == null) {
            synchronized (AppContainer.class) {
                if (sessionRepository == null) {
                    sessionRepository = new SharedPrefsSessionRepository(context);
                }
            }
        }
        return sessionRepository;
    }

    public static ApiClientProvider apiClientProvider(Context context) {
        if (apiClientProvider == null) {
            synchronized (AppContainer.class) {
                if (apiClientProvider == null) {
                    apiClientProvider = new ApiClientProvider(sessionRepository(context));
                }
            }
        }
        return apiClientProvider;
    }
}
