package com.example.android_java.Feature.Home;

public final class HomeRefreshBus {
    public static final class RefreshPayload {
        public final String topic;
        public final String contentId;

        public RefreshPayload(String topic, String contentId) {
            this.topic = topic;
            this.contentId = contentId;
        }
    }

    private static RefreshPayload pendingPayload;

    private HomeRefreshBus() {
    }

    public static synchronized void publish(String topic, String contentId) {
        pendingPayload = new RefreshPayload(topic, contentId);
    }

    public static synchronized RefreshPayload consume() {
        RefreshPayload payload = pendingPayload;
        pendingPayload = null;
        return payload;
    }
}
