package com.example.paginationexample.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.paginationexample.data.mappers.ImageDTOToImageMapper
import com.example.paginationexample.data.pagingSource.ImagePagingSource
import com.example.paginationexample.domain.model.Image
import com.example.paginationexample.data.remote.ImagesApi
import com.example.paginationexample.domain.repositroy.ImageRepository

class ImageRepositoryImpl(private val api: ImagesApi, private val mapper: ImageDTOToImageMapper) :
    ImageRepository {

    override fun getImages(query: String): Pager<Int, Image> =
        Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 1, initialLoadSize = 20),
            pagingSourceFactory = {
                ImagePagingSource(imagesApi = api, query = query, mapper = mapper)
            })
}