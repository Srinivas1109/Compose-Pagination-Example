package com.example.paginationexample.data.di

import com.example.paginationexample.data.remote.ImagesApi
import com.example.paginationexample.data.remote.ImagesApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module{
    single{
        HttpClient(OkHttp){
            install(ContentNegotiation){
                json(Json{
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    singleOf(::ImagesApiImpl).bind<ImagesApi>()
}
