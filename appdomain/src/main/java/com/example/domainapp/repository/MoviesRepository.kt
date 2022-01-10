package com.example.domainapp.repository

import androidx.lifecycle.LiveData
import com.example.domainapp.database.MoviesDao
import com.example.domainapp.database.MoviesEntity
import javax.inject.Inject

class MoviesRepository @Inject constructor ( val moviesDao: MoviesDao) {


    val readFavouriteMoviesEntity : LiveData<List<MoviesEntity>> = moviesDao.readFavouriteMovies()

    suspend fun addMovieToFavourites(movie: MoviesEntity){
        moviesDao.addMovieToFavourites(movie)
    }

}