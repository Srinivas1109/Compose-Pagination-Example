package com.example.paginationexample.domain.repositroy

import androidx.paging.Pager
import com.example.paginationexample.domain.model.Image

interface ImageRepository {
    fun getImages(query: String) : Pager<Int, Image>
}