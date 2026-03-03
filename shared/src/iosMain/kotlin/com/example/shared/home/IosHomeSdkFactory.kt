package com.example.shared.home

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object IosHomeSdkFactory {
    fun create(baseUrl: String, token: String): HomeSdk {
        val client = HttpClient(Darwin) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            defaultRequest {
                url(baseUrl)
                contentType(ContentType.Application.Json)
                if (token.isNotBlank()) {
                    headers.append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }

        val repository = HomeRepository(KtorHomeDataSource(client))
        return HomeSdk(
            getTopicsUseCase = GetTopicsUseCase(repository),
            getFeedUseCase = GetFeedUseCase(repository)
        )
    }
}
