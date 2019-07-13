package com.hongwei.remember_the_milk_api_sample.injection.component

import com.hongwei.remember_the_milk_api_sample.injection.annotations.PerFragment
import com.hongwei.remember_the_milk_api_sample.injection.modules.FragmentModule
import dagger.Component

@PerFragment
@Component(dependencies = [ActivityComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {

}