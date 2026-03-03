package com.example.android_java.Feature.Content;

import com.example.android_java.Data.Model.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentGateway {

    public interface Callback<T> {
        void onSuccess(T data);

        void onError(String message);
    }

    private final ContentApi contentApi;

    public ContentGateway(ContentApi contentApi) {
        this.contentApi = contentApi;
    }

    public void createPost(String title, String summary, String topicName, Callback<ContentDtos.CreatePostData> callback) {
        ContentDtos.CreatePostRequest request = new ContentDtos.CreatePostRequest();
        request.title = title;
        request.summary = summary;
        request.topicName = topicName;

        contentApi.createPost(request).enqueue(new Callback<ApiResponse<ContentDtos.CreatePostData>>() {
            @Override
            public void onResponse(Call<ApiResponse<ContentDtos.CreatePostData>> call, Response<ApiResponse<ContentDtos.CreatePostData>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    callback.onError("create_post_failed");
                    return;
                }
                callback.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<ApiResponse<ContentDtos.CreatePostData>> call, Throwable t) {
                callback.onError("create_post_error");
            }
        });
    }

    public void like(String contentId, Callback<ContentDtos.LikeData> callback) {
        contentApi.like(contentId).enqueue(new Callback<ApiResponse<ContentDtos.LikeData>>() {
            @Override
            public void onResponse(Call<ApiResponse<ContentDtos.LikeData>> call, Response<ApiResponse<ContentDtos.LikeData>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    callback.onError("like_failed");
                    return;
                }
                callback.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<ApiResponse<ContentDtos.LikeData>> call, Throwable t) {
                callback.onError("like_error");
            }
        });
    }

    public void createComment(String contentId, String comment, Callback<ContentDtos.CreateCommentData> callback) {
        ContentDtos.CreateCommentRequest request = new ContentDtos.CreateCommentRequest();
        request.comment = comment;

        contentApi.createComment(contentId, request).enqueue(new Callback<ApiResponse<ContentDtos.CreateCommentData>>() {
            @Override
            public void onResponse(Call<ApiResponse<ContentDtos.CreateCommentData>> call, Response<ApiResponse<ContentDtos.CreateCommentData>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    callback.onError("comment_failed");
                    return;
                }
                callback.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<ApiResponse<ContentDtos.CreateCommentData>> call, Throwable t) {
                callback.onError("comment_error");
            }
        });
    }

    public void listComments(String contentId, String cursor, Integer limit, Callback<ContentDtos.CommentListData> callback) {
        contentApi.comments(contentId, cursor, limit).enqueue(new Callback<ApiResponse<ContentDtos.CommentListData>>() {
            @Override
            public void onResponse(Call<ApiResponse<ContentDtos.CommentListData>> call, Response<ApiResponse<ContentDtos.CommentListData>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    callback.onError("comments_failed");
                    return;
                }
                callback.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<ApiResponse<ContentDtos.CommentListData>> call, Throwable t) {
                callback.onError("comments_error");
            }
        });
    }

    public void listComments(String contentId, Callback<ContentDtos.CommentListData> callback) {
        listComments(contentId, null, 20, callback);
    }

    public void deleteLatestOwnComment(String contentId, Callback<ContentDtos.DeleteCommentData> callback) {
        contentApi.deleteLatestOwnComment(contentId).enqueue(new Callback<ApiResponse<ContentDtos.DeleteCommentData>>() {
            @Override
            public void onResponse(Call<ApiResponse<ContentDtos.DeleteCommentData>> call, Response<ApiResponse<ContentDtos.DeleteCommentData>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    callback.onError("delete_comment_failed");
                    return;
                }
                callback.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<ApiResponse<ContentDtos.DeleteCommentData>> call, Throwable t) {
                callback.onError("delete_comment_error");
            }
        });
    }
}
