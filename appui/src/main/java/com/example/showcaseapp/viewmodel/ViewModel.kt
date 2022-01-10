package com.example.showcaseapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domainapp.database.MoviesDatabase
import com.example.domainapp.database.MoviesEntity
//import com.example.finalProject.di.ApiService
import com.example.domainapp.models.Movies
import com.example.domainapp.models.Results
import com.example.domainapp.network.ApiService
//import com.example.domainapp.network.RetroInstance
import com.example.domainapp.repository.MoviesRepository
import com.example.showcaseapp.MyApplication
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import java.util.*
import javax.inject.Inject

class ViewModel(application: Application): AndroidViewModel(application) {

    private val TAG = "ViewModel"
    var topRatedMoviesEntityLiveData: MutableLiveData<List<Results>>
    var popularMoviesEntityLiveData: MutableLiveData<List<Results>>
    var readFavouriteMoviesEntity : LiveData<List<MoviesEntity>>
    private val compositeDisposable = CompositeDisposable()


    @Inject
    lateinit var apiService: ApiService

//    @Inject
//    lateinit var moviesDao:MoviesDao

    private var repository: com.example.domainapp.repository.MoviesRepository

    init {
        (application as MyApplication).getApiComponent().inject(this)
        topRatedMoviesEntityLiveData = MutableLiveData<List<Results>>()
        popularMoviesEntityLiveData = MutableLiveData<List<Results>>()
        val moviesDao = MoviesDatabase.getDatabase(application).moviesDao()
        repository = MoviesRepository(moviesDao)
        readFavouriteMoviesEntity = repository.readFavouriteMoviesEntity
    }

    fun makeApiCall() {
//        val apiService = RetroInstance.getRetroClient()?.create(
//            com.example.domainapp.network.ApiService::class.java)
        val getTopRatedMoviesEntity: Observable<Movies> =
            apiService.getTopRatedMovies("e807e46843c45d1ac8631a914207e53e")

        val getPopularMoviesEntity: Observable<Movies> =
            apiService.getPopularMovies("e807e46843c45d1ac8631a914207e53e")

//        getJsonResponse(getTopRatedMoviesEntity,0)
//        getJsonResponse(getPopularMoviesEntity,1)


        getDataFromObservable(getTopRatedMoviesEntity,0)
        getDataFromObservable(getPopularMoviesEntity,1)


    }

    private fun getDataFromObservable(getTopRatedMoviesEntity: Observable<Movies>, identifier:Int){

        getObservableMovies(getTopRatedMoviesEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Results>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: List<Results>) {
                    if(identifier == 0){
                        topRatedMoviesEntityLiveData.value = t
                    }
                    else{
                        popularMoviesEntityLiveData.value = t
                    }
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }


            })


    }

    private fun getObservableMovies(getTopRatedMoviesEntity: Observable<Movies>):Observable<List<Results>>{
        return getTopRatedMoviesEntity
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                return@flatMap Observable.just(it.results)
            }
    }

    private fun getJsonResponse(getTopRatedMoviesEntity: Call<Movies>, identifier:Int) {
//        getTopRatedMoviesEntity.enqueue(object : Callback<Movies> {
//            override fun onResponse(
//                call: Call<Movies>,
//                response: Response<Movies>) {
//
//                val responses = response.body()
//                if(responses!=null) {
//                    if(identifier==0)
//                        topRatedMoviesEntityLiveData!!.value=responses!!
//                    else{
//                        popularMoviesEntityLiveData!!.value = responses!!
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<Movies>, t: Throwable) {
//                Log.d(TAG, "${t.message}")
//            }
//        })
    }

    fun getTopRatedMoviesMutableLiveData():MutableLiveData<List<Results>>{
        return topRatedMoviesEntityLiveData
    }

    fun getPopularMoviesMutableLiveData():MutableLiveData<List<Results>>{
        return popularMoviesEntityLiveData
    }

    fun addMovieToFavourites(movie:MoviesEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovieToFavourites(movie)
        }
    }

    fun getFavouriteMovies():LiveData<List<MoviesEntity>>{
        return readFavouriteMoviesEntity
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()

    }
}