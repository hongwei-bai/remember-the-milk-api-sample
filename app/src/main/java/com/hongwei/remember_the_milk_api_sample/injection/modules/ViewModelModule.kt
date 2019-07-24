package com.hongwei.remember_the_milk_api_sample.injection.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hongwei.remember_the_milk_api_sample.injection.utils.ViewModelFactory
import com.hongwei.remember_the_milk_api_sample.injection.utils.ViewModelKey
import com.hongwei.remember_the_milk_api_sample.presentation.main.ApiConfigViewModel
import com.hongwei.remember_the_milk_api_sample.presentation.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ApiConfigViewModel::class)
    internal abstract fun bindApiConfigViewModel(apiConfigViewModel: ApiConfigViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
