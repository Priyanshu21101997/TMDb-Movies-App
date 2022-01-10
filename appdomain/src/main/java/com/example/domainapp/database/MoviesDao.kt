package com.example.domainapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMovieToFavourites(movie: MoviesEntity)

    @Query("SELECT * from movies_table order by movieId desc")
    fun readFavouriteMovies(): LiveData<List<MoviesEntity>>

}