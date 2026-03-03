package com.example.android_java.Feature.Topic;

import com.example.android_java.Data.Model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TopicApi {

    @GET("v1/topics")
    Call<ApiResponse<TopicDtos.TopicListData>> topics();
}
