package com.example.showcaseapp

import android.app.Application
import com.example.di.DaggerAppComponent
import com.example.di.AppComponent
import com.example.di.AppModule

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