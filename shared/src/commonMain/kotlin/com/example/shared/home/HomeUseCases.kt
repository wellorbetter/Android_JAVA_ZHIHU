package com.example.shared.home

class GetTopicsUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(): List<Topic> = repository.topics()
}

class GetFeedUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(channel: FeedChannel, topicName: String?): List<FeedItem> =
        repository.feed(channel, topicName)
}
