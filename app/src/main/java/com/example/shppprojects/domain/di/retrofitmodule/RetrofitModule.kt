package com.example.shppprojects.domain.di.retrofitmodule

import com.example.shppprojects.domain.network.AccountApiService
import com.example.shppprojects.domain.network.ContactsApiService
import com.example.shppprojects.presentation.utils.Constants.BASE_URL
import com.example.shppprojects.presentation.utils.MyResponseInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Singleton
    @Provides
    fun providesMyResponseInterceptor(): MyResponseInterceptor = MyResponseInterceptor()

    @Singleton
    @Provides
    fun providesOkHttpClient(
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor(MyResponseInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideConvertorFactory(): GsonConverterFactory {
        return GsonConverterFactory
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        convertorFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(convertorFactory)
            .build()
    }

    @Provides
    @Singleton
    fun providesAccountApiService(retrofit: Retrofit): AccountApiService {
        return retrofit.create(AccountApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesContactsApiService(retrofit: Retrofit): ContactsApiService {
        return retrofit.create(ContactsApiService::class.java)
    }

}