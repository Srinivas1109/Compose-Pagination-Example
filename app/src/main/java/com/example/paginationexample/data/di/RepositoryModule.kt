package com.example.paginationexample.data.di

import com.example.paginationexample.data.mappers.ImageDTOToImageMapper
import com.example.paginationexample.domain.repositroy.ImageRepository
import com.example.paginationexample.data.repository.ImageRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module{
    singleOf(::ImageRepositoryImpl).bind<ImageRepository>()
    singleOf(::ImageDTOToImageMapper)
}
