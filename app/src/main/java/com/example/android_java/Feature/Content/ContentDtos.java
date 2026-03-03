package com.example.android_java.Feature.Content;

import java.util.List;

public final class ContentDtos {

    private ContentDtos() {
    }

    public static class CreatePostRequest {
        public String title;
        public String summary;
        public String topicName;
    }

    public static class CreatePostData {
        public boolean created;
        public String contentId;
        public String error;
    }

    public static class LikeData {
        public boolean liked;
        public String contentId;
        public int likeCount;
        public String error;
    }

    public static class CreateCommentRequest {
        public String comment;
    }

    public static class CreateCommentData {
        public boolean commented;
        public String commentId;
        public String contentId;
        public int commentCount;
        public String error;
    }

    public static class CommentItem {
        public String commentId;
        public String contentId;
        public String authorName;
        public String comment;
        public long createdAt;
    }

    public static class CommentListData {
        public List<CommentItem> items;
        public String nextCursor;
        public boolean hasMore;
        public int totalCount;
    }

    public static class DeleteCommentData {
        public boolean deleted;
        public String commentId;
        public String contentId;
        public int commentCount;
        public String error;
    }
}
