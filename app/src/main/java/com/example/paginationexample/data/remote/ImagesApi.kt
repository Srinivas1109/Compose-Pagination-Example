package com.example.paginationexample.data.remote

import com.example.paginationexample.data.model.remote.ImageNetworkResponse

interface ImagesApi {
    suspend fun getImages(query: String, page: Int, perPage: Int = 20): ImageNetworkResponse
}