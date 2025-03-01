package com.ralphevmanzano.catlibrary.di

import com.ralphevmanzano.catlibrary.BuildConfig
import com.ralphevmanzano.catlibrary.data.remote.ApiKeyInterceptor
import com.ralphevmanzano.catlibrary.data.remote.CatService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val networkModule = module {
    single {
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
    }

    single {
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    this.addNetworkInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                }
                addNetworkInterceptor(ApiKeyInterceptor())
            }.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(get())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single {
        get<Retrofit>().create(CatService::class.java)
    }
}