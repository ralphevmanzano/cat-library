package com.ralphevmanzano.catlibrary.di

import com.ralphevmanzano.catlibrary.BuildConfig
import com.ralphevmanzano.catlibrary.data.remote.ApiKeyInterceptor
import com.ralphevmanzano.catlibrary.data.remote.CatService
import com.ralphevmanzano.catlibrary.data.remote.ImageService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

fun provideJson(): Json {
    return Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                this.addNetworkInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }
            readTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
            addNetworkInterceptor(ApiKeyInterceptor())
        }.build()
}

fun provideCatRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.CAT_API_BASE_URL)
        .client(provideOkHttpClient())
        .addConverterFactory(provideJson().asConverterFactory("application/json".toMediaType()))
        .build()
}

fun provideCatService(retrofit: Retrofit): CatService {
    return retrofit.create(CatService::class.java)
}

fun provideImageRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.CAT_API_IMAGES_CDN)
        .build()
}

fun provideImageService(retrofit: Retrofit): ImageService {
    return retrofit.create(ImageService::class.java)
}

val networkModule = module {
    single { provideJson() }
    single { provideOkHttpClient() }

    single(qualifier = catRetrofitQualifier) { provideCatRetrofit() }
    single { provideCatService(get(qualifier = catRetrofitQualifier)) }

    single(qualifier = imageRetrofitQualifier) { provideImageRetrofit() }
    single { provideImageService(get(qualifier = imageRetrofitQualifier)) }
}