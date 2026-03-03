package com.example.android_java.Core.Network;

import androidx.annotation.NonNull;

import com.example.android_java.Core.Session.SessionRepository;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final SessionRepository sessionRepository;

    public AuthInterceptor(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String token = sessionRepository.getAccessToken();

        if (token != null && !token.trim().isEmpty()) {
            request = request.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
        }

        return chain.proceed(request);
    }
}
