package com.example.domainapp.di

import com.example.domainapp.network.ApiService
//import com.example.domainapp.network.RetroInstance.Companion.retrofit
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
class apiModule {
    private val BASE_URL = "https://api.themoviedb.org/3/"

    @Provides
    @Singleton
    fun getRetroClient(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun getApiServiceInterface(retrofit: Retrofit):ApiService{
        return retrofit.create(ApiService::class.java)
    }
}