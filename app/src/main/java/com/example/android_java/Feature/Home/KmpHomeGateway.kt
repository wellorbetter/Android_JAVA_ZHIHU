package com.example.android_java.Feature.Home

import android.content.Context
import com.example.android_java.Bean.HomePage.HomePageRecommendedNewsItem
import com.example.android_java.Core.AppContainer
import com.example.android_java.Core.Network.AppEnvironment
import com.example.shared.home.AccessTokenProvider
import com.example.shared.home.AndroidHomeSdkFactory
import com.example.shared.home.FeedChannel
import com.example.shared.home.FeedItem
import com.example.shared.home.HomeCallback
import com.example.shared.home.HomeSdk
import com.example.shared.home.Topic

class KmpHomeGateway(context: Context) {

    interface Callback<T> {
        fun onSuccess(data: T)
        fun onError(message: String)
    }

    private val sdk: HomeSdk

    init {
        val sessionRepository = AppContainer.sessionRepository(context.applicationContext)
        sdk = AndroidHomeSdkFactory.create(
            AppEnvironment.BASE_URL,
            AccessTokenProvider { sessionRepository.accessToken }
        )
    }

    fun loadTopics(callback: Callback<List<String>>) {
        sdk.loadTopics(object : HomeCallback<List<Topic>> {
            override fun onSuccess(data: List<Topic>) {
                callback.onSuccess(data.map { it.name })
            }

            override fun onError(message: String) {
                callback.onError(message)
            }
        })
    }

    fun loadFeed(channel: String, topicName: String, callback: Callback<List<HomePageRecommendedNewsItem>>) {
        sdk.loadFeed(toChannel(channel), topicName, object : HomeCallback<List<FeedItem>> {
            override fun onSuccess(data: List<FeedItem>) {
                callback.onSuccess(
                    data.map {
                        val mapped = HomePageRecommendedNewsItem(
                            it.title,
                            it.summary,
                            it.topicName
                        )
                        mapped.contentId = it.contentId
                        mapped.likeCount = it.likeCount
                        mapped.commentCount = it.commentCount
                        mapped
                    }
                )
            }

            override fun onError(message: String) {
                callback.onError(message)
            }
        })
    }

    private fun toChannel(channel: String): FeedChannel {
        return when (channel) {
            "idea" -> FeedChannel.IDEA
            "following" -> FeedChannel.FOLLOWING
            "hot" -> FeedChannel.HOT
            else -> FeedChannel.RECOMMEND
        }
    }

    fun close() {
        sdk.close()
    }
}
