package com.example.shared.home

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiEnvelope<T>(
    val code: Int,
    val message: String,
    val data: T? = null
)

@Serializable
internal data class TopicListPayload(
    val topics: List<TopicDto> = emptyList()
)

@Serializable
internal data class TopicDto(
    val topicId: String,
    val topicName: String,
    val description: String = ""
)

@Serializable
internal data class FeedPagePayload(
    val items: List<FeedItemDto> = emptyList()
)

@Serializable
internal data class FeedItemDto(
    val contentId: String,
    val title: String,
    val summary: String,
    val authorName: String,
    val likeCount: Int,
    val commentCount: Int,
    val topicName: String
)
