package com.example.domainapp.database

import android.text.TextUtils
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.domainapp.models.Results
import com.google.gson.Gson

@Entity(tableName = "movies_table")
data class MoviesEntity(

    @PrimaryKey(autoGenerate = true)  var movieId : Int? = null,
    var id:Int? = null,
    var result : Results? = null
){
}


class RequestConverter {
    @TypeConverter
    fun stringToOutboxItem(string: String): Results? {
        if (TextUtils.isEmpty(string))
            return null
        return Gson().fromJson(string, Results::class.java)
    }

    @TypeConverter
     fun outboxItemToString(result: Results?): String {
        return Gson().toJson(result)
    }
}