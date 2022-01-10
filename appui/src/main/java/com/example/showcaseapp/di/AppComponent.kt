package com.example.showcaseapp.di

import com.example.showcaseapp.viewmodel.ViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(viewModel: ViewModel)

}