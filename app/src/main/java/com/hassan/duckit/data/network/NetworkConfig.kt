package com.hassan.duckit.data.network

data class NetworkConfig(
    val baseUrl: String,
    val connectTimeout: Long = 30L,
    val readTimeout: Long = 30L,
    val writeTimeout: Long = 30L
)
