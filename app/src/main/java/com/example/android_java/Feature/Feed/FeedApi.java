package com.example.android_java.Feature.Feed;

import com.example.android_java.Data.Model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FeedApi {

    @GET("v1/feed/recommend")
    Call<ApiResponse<FeedDtos.FeedPage>> recommend(@Query("cursor") String cursor);

    @GET("v1/feed/following")
    Call<ApiResponse<FeedDtos.FeedPage>> following(@Query("cursor") String cursor);
}
