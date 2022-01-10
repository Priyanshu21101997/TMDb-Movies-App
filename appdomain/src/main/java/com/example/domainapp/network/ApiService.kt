package com.example.domainapp.network

import com.example.domainapp.models.Movies
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

 interface ApiService {

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") api_key:String): Observable<Movies>

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") api_key:String): Observable<Movies>

}