package com.example.paginationexample.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class ImageNetworkResponse(
    val hits: List<ImageDTO>,
    val total: Int,
    val totalHits: Int
)