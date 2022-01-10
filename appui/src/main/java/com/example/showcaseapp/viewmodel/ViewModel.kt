package com.example.showcaseapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.domainapp.database.MoviesDao
import com.example.domainapp.database.MoviesEntity
import com.example.domainapp.models.Movies
import com.example.domainapp.models.Results
import com.example.domainapp.network.ApiService
import com.example.domainapp.repository.MoviesRepository
import com.example.showcaseapp.MyApplication
import com.example.showcaseapp.utils.Constants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ViewModel public constructor(application: Application): AndroidViewModel(application) {

    private val TAG = "ViewModel"
    var topRatedMoviesEntityLiveData: MutableLiveData<List<Results>>
    var popularMoviesEntityLiveData: MutableLiveData<List<Results>>
    private var readFavouriteMoviesEntity : LiveData<List<MoviesEntity>>
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var moviesDao: MoviesDao

    @Inject
    lateinit var repository: MoviesRepository

    init {
        (application as MyApplication).getApiComponent().inject(this)
        topRatedMoviesEntityLiveData = MutableLiveData<List<Results>>()
        popularMoviesEntityLiveData = MutableLiveData<List<Results>>()
        readFavouriteMoviesEntity = repository.readFavouriteMoviesEntity
    }

    fun makeApiCall() {

        val getTopRatedMoviesEntity: Observable<Movies> =
            apiService.getTopRatedMovies(Constants.API_KEY)

        val getPopularMoviesEntity: Observable<Movies> =
            apiService.getPopularMovies(Constants.API_KEY)

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