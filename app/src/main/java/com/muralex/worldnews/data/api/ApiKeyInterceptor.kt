package com.muralex.worldnews.data.api

import okhttp3.Interceptor
import okhttp3.Response

open class ApiKeyInterceptor (private val apiKeyParam: String, private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newHttpUrl = originalRequest.url.newBuilder()
            .addQueryParameter(apiKeyParam, apiKey)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newHttpUrl)
            .build()
        return chain.proceed(newRequest)
    }
}

class NewsApiKeyInterceptor (
    private val apiKeyParam: String,
    private val apiKey: String) : ApiKeyInterceptor(apiKeyParam, apiKey)

