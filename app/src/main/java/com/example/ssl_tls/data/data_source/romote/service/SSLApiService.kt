package com.example.ssl_tls.data.data_source.romote.service

import okhttp3.ResponseBody
import retrofit2.http.GET

interface SSLApiService {

    @GET("/")
    suspend fun getSupardatingApi(): ResponseBody
}