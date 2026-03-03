package com.example.shared.home

interface HomeCallback<T> {
    fun onSuccess(data: T)
    fun onError(message: String)
}
