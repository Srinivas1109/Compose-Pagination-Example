package com.example.paginationexample.data.mappers

import com.example.paginationexample.data.model.remote.ImageDTO
import com.example.paginationexample.domain.model.Image
import java.util.UUID

class ImageDTOToImageMapper : Mapper<ImageDTO, Image> {
    override fun map(from: ImageDTO): Image {
        return Image(
            id = from.id.toString(),
            uuid = UUID.randomUUID().toString(),
            imageUrl = from.largeImageURL
        )
    }
}