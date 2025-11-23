package com.example.ssl_tls.data.data_source.romote.service

import android.R.attr.level
import android.content.Context
import com.example.ssl_tls.BuildConfig.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder (
    private val context: Context
){

    val sslProvider = SslProvider()

    private val networkInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(interceptor = networkInterceptor)
            // method 1
          //  .certificatePinner(sslProvider.createCertificatePinning())
            // method 2
            .addSSLSocketFactory(context = context)
            .build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
       .client(okHttpClient)
        .build()


    fun getSSLApiService(): SSLApiService {
        return retrofit.create(SSLApiService::class.java)
    }


}