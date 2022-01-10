package com.example.showcaseapp

import android.app.Application
import com.example.domainapp.di.DaggerapiComponent
import com.example.domainapp.di.apiComponent
import com.example.domainapp.di.apiModule

class MyApplication:Application() {

    private lateinit var apiComponent:apiComponent

    override fun onCreate() {
        super.onCreate()

        apiComponent = DaggerapiComponent.builder().apiModule(apiModule()).build()
    }

    fun getApiComponent(): apiComponent {
        return apiComponent
    }
}