package com.example.paginationexample.data.mappers

interface Mapper<F, T> {
    fun map(from: F): T
}

fun <F, T> Mapper<F, T>.mapAll(list: List<F>): List<T> = list.map { map(it) }