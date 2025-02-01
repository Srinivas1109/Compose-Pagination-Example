package com.example.paginationexample

import android.app.Application
import com.example.paginationexample.data.di.mainModule
import com.example.paginationexample.data.di.networkModule
import com.example.paginationexample.data.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PagingExampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@PagingExampleApp)
            modules(networkModule, repositoryModule, mainModule)
        }
    }
}