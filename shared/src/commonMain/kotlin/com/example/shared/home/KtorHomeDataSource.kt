package com.example.shared.home

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorHomeDataSource(
    private val client: HttpClient
) : HomeDataSource {

    override suspend fun loadTopics(): List<Topic> {
        val response = client.get("v1/topics").body<ApiEnvelope<TopicListPayload>>()
        if (response.code != 0 || response.data == null) {
            throw IllegalStateException("load_topics_failed")
        }
        return response.data.topics.map { Topic(it.topicId, it.topicName, it.description) }
    }

    override suspend fun loadFeed(channel: FeedChannel, topicName: String?): List<FeedItem> {
        val response = client.get(channel.path) {
            if (!topicName.isNullOrBlank() && topicName != "All") {
                parameter("topic", topicName)
            }
        }.body<ApiEnvelope<FeedPagePayload>>()

        if (response.code != 0 || response.data == null) {
            throw IllegalStateException("load_feed_failed")
        }
        return response.data.items.map {
            FeedItem(
                contentId = it.contentId,
                title = it.title,
                summary = it.summary,
                authorName = it.authorName,
                likeCount = it.likeCount,
                commentCount = it.commentCount,
                topicName = it.topicName
            )
        }
    }
}
