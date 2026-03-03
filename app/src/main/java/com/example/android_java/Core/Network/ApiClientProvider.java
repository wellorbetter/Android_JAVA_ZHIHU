package com.example.android_java.Core.Network;

import com.example.android_java.Core.Session.SessionRepository;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientProvider {

    private final Retrofit retrofit;

    public ApiClientProvider(SessionRepository sessionRepository) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(sessionRepository))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(AppEnvironment.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T create(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
