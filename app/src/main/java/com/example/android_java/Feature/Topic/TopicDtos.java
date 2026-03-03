package com.example.android_java.Feature.Topic;

import java.util.List;

public final class TopicDtos {

    private TopicDtos() {
    }

    public static class TopicItem {
        public String topicId;
        public String topicName;
        public String iconUrl;
        public String description;
    }

    public static class TopicListData {
        public List<TopicItem> topics;
    }
}
