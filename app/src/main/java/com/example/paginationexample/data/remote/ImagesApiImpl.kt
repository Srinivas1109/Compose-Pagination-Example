package com.example.paginationexample.data.remote

import android.util.Log
import com.example.paginationexample.data.model.remote.ImageNetworkResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url

class ImagesApiImpl(val client: HttpClient) : ImagesApi {
    override suspend fun getImages(query: String, page: Int, perPage: Int): ImageNetworkResponse {
        Log.i("PAGE_LOAD", Url("$BASE_URL/api/?key=$API_KEY&q=$query&page=$page").toString())
        val response: ImageNetworkResponse =
            client.get(Url("$BASE_URL/api/?key=$API_KEY&q=$query&page=$page"))
                .body()
        return response
    }

    companion object {
        const val BASE_URL = "https://pixabay.com"
        private const val API_KEY = "48559995-f626ba08c047183d6220a7beb"
    }
}