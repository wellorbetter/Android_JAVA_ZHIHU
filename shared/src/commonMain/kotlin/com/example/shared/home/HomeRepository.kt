package com.example.shared.home

enum class FeedChannel(val path: String) {
    IDEA("v1/feed/idea"),
    FOLLOWING("v1/feed/following"),
    RECOMMEND("v1/feed/recommend"),
    HOT("v1/feed/hot")
}

interface HomeDataSource {
    suspend fun loadTopics(): List<Topic>
    suspend fun loadFeed(channel: FeedChannel, topicName: String?): List<FeedItem>
}

class HomeRepository(private val dataSource: HomeDataSource) {
    suspend fun topics(): List<Topic> = dataSource.loadTopics()
    suspend fun feed(channel: FeedChannel, topicName: String?): List<FeedItem> = dataSource.loadFeed(channel, topicName)
}
