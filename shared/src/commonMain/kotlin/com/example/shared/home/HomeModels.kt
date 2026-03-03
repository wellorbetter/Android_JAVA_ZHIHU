package com.example.shared.home

data class Topic(
    val id: String,
    val name: String,
    val description: String
)

data class FeedItem(
    val contentId: String,
    val title: String,
    val summary: String,
    val authorName: String,
    val likeCount: Int,
    val commentCount: Int,
    val topicName: String
)
