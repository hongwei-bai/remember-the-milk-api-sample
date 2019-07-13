package com.hongwei.remember_the_milk_api_sample.injection.utils;

import androidx.lifecycle.ViewModel;
import dagger.MapKey;

import java.lang.annotation.*;

@Documented
@MapKey
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewModelKey {

    Class<? extends ViewModel> value();
}
