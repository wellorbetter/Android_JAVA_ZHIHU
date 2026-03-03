package com.example.android_java.Feature.Feed;

import java.util.List;

public final class FeedDtos {

    private FeedDtos() {
    }

    public static class FeedItem {
        public String contentId;
        public String title;
        public String summary;
        public String authorName;
        public int likeCount;
        public int commentCount;
        public String topicName;
    }

    public static class FeedPage {
        public List<FeedItem> items;
        public String nextCursor;
        public boolean hasMore;
    }
}
