package com.example.domainapp.di

import androidx.lifecycle.ViewModel
import dagger.Component

@Component(modules = [apiModule::class])
interface apiComponent {

    fun inject(viewModel: ViewModel)
}