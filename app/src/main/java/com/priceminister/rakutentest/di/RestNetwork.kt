package com.priceminister.rakutentest.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.priceminister.rakutentest.network.RakutenApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ViewModelComponent::class)
object RestNetwork {

    @ViewModelScoped
    @Provides
    fun provideBaseURL(): String {
        return "https://4206121f-64a1-4256-a73d-2ac541b3efe4.mock.pstmn.io/"
    }

    @ViewModelScoped
    @Provides
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @ViewModelScoped
    @Provides
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
            val url = request.url
            val builder = url.newBuilder()
            requestBuilder.url(builder.build())
                .method(request.method, request.body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")

            chain.proceed(requestBuilder.build())
        }
    }

    @ViewModelScoped
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @ViewModelScoped
    @Provides
    fun provideOkHttp(
        interceptor: HttpLoggingInterceptor,
        headerInterceptor: Interceptor,
        @ApplicationContext appContext: Context
    ): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder();
        okHttpClient.callTimeout(60, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(30, TimeUnit.SECONDS)
        okHttpClient.readTimeout(60, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(headerInterceptor)
        okHttpClient.addInterceptor(interceptor)

        return okHttpClient.build();
    }


    @ViewModelScoped
    @Provides
    fun provideRestAdapter(
        baseURL: String,
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        val retro = Retrofit.Builder().baseUrl(baseURL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retro;
    }

    @ViewModelScoped
    @Provides
    fun provideRakutenApi(retrofit: Retrofit): RakutenApi {
        return retrofit.create(RakutenApi::class.java)
    }
}