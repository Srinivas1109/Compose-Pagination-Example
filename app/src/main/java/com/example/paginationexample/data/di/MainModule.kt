package com.example.paginationexample.data.di

import com.example.paginationexample.presentation.ui.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module{
    viewModelOf(::MainViewModel)
}