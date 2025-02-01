package com.example.paginationexample.data.pagingSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paginationexample.data.mappers.ImageDTOToImageMapper
import com.example.paginationexample.data.mappers.mapAll
import com.example.paginationexample.domain.model.Image
import com.example.paginationexample.data.remote.ImagesApi

class ImagePagingSource(
    private val imagesApi: ImagesApi,
    private val query: String,
    private val mapper: ImageDTOToImageMapper
) : PagingSource<Int, Image>() {
    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        return try {
            val nextPage = params.key ?: 1
            val pageSize = params.loadSize
            val imagesResponse = imagesApi.getImages(query, nextPage, pageSize)
            LoadResult.Page(
                data = mapper.mapAll(imagesResponse.hits),
                prevKey = if (nextPage == 1) null else nextPage.minus(1),
                nextKey = nextPage.plus(1)
            )
        } catch (e: Exception) {
            Log.e("LOAD_ERROR", e.message.toString())
            LoadResult.Error(e)
        }
    }
}