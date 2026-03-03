package com.example.android_java.Feature.Auth;

import com.example.android_java.Data.Model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("v1/auth/login")
    Call<ApiResponse<AuthDtos.LoginData>> login(@Body AuthDtos.LoginRequest request);

    @POST("v1/auth/logout")
    Call<ApiResponse<Object>> logout();
}
