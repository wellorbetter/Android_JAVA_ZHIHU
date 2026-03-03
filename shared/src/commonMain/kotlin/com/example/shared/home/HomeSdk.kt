package com.example.shared.home

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomeSdk(
    private val getTopicsUseCase: GetTopicsUseCase,
    private val getFeedUseCase: GetFeedUseCase,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {

    fun loadTopics(callback: HomeCallback<List<Topic>>) {
        scope.launch {
            try {
                callback.onSuccess(getTopicsUseCase())
            } catch (t: Throwable) {
                callback.onError(t.message ?: "load_topics_failed")
            }
        }
    }

    fun loadFeed(channel: FeedChannel, topicName: String?, callback: HomeCallback<List<FeedItem>>) {
        scope.launch {
            try {
                callback.onSuccess(getFeedUseCase(channel, topicName))
            } catch (t: Throwable) {
                callback.onError(t.message ?: "load_feed_failed")
            }
        }
    }

    fun close() {
        scope.cancel()
    }
}
