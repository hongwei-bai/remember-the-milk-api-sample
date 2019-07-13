package com.hongwei.remember_the_milk_api_sample.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.hongwei.remember_the_milk_api_sample.MyApplication
import com.hongwei.remember_the_milk_api_sample.injection.component.ActivityComponent
import com.hongwei.remember_the_milk_api_sample.injection.utils.ViewModelFactory
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var activityComponent: ActivityComponent

    private val EXTRA_SLIDE_BACK = "EXTRA_SLIDE_BACK"
    private var slideBack = false

    protected fun getAppComponent() = (applicationContext as MyApplication).applicationComponent

    abstract fun inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        slideBack = intent.extras?.getBoolean(EXTRA_SLIDE_BACK) ?: false
    }

    fun getViewModelProvider(activity: FragmentActivity): ViewModelProvider {
        return ViewModelProviders.of(activity, viewModelFactory)
    }

    protected fun setBackActionBarButton(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = ""
    }
}