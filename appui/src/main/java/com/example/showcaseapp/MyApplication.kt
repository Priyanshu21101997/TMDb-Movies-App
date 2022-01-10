package com.example.showcaseapp

import android.app.Application
import com.example.showcaseapp.di.AppComponent
import com.example.showcaseapp.di.AppModule
import com.example.showcaseapp.di.DaggerAppComponent

class MyApplication:Application() {

    private lateinit var AppComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        AppComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    fun getApiComponent(): AppComponent {
        return AppComponent
    }
}