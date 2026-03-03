package com.example.android_java.Feature.Content;

import com.example.android_java.Data.Model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContentApi {

    @POST("v1/content/posts")
    Call<ApiResponse<ContentDtos.CreatePostData>> createPost(@Body ContentDtos.CreatePostRequest request);

    @POST("v1/content/{contentId}/like")
    Call<ApiResponse<ContentDtos.LikeData>> like(@Path("contentId") String contentId);

    @POST("v1/content/{contentId}/comments")
    Call<ApiResponse<ContentDtos.CreateCommentData>> createComment(@Path("contentId") String contentId, @Body ContentDtos.CreateCommentRequest request);

    @GET("v1/content/{contentId}/comments")
    Call<ApiResponse<ContentDtos.CommentListData>> comments(@Path("contentId") String contentId,
                                                            @Query("cursor") String cursor,
                                                            @Query("limit") Integer limit);

    @POST("v1/content/{contentId}/comments/delete-latest")
    Call<ApiResponse<ContentDtos.DeleteCommentData>> deleteLatestOwnComment(@Path("contentId") String contentId);
}
