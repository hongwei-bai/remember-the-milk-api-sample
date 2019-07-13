package com.hongwei.remember_the_milk_api_sample.presentation.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.hongwei.remember_the_milk_api_sample.R
import com.hongwei.remember_the_milk_api_sample.injection.component.ActivityComponent

abstract class BaseFragment : Fragment() {

    enum class StatusBarDisplayType {
        LIGHT,
        DARK
    }

    abstract fun inject()

    abstract fun getStatusBarDisplayType(): StatusBarDisplayType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inject()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            setupStatusBarDisplay(getStatusBarDisplayType())
        }
    }

    fun getActivityComponent(): ActivityComponent {
        return (activity as BaseActivity).activityComponent
    }

    fun getViewModelProvider(activity: FragmentActivity): ViewModelProvider {
        val viewModelFactory = (activity as BaseActivity).viewModelFactory
        return ViewModelProviders.of(activity, viewModelFactory)
    }

    private fun setupStatusBarDisplay(statusBarDisplayType: StatusBarDisplayType) {
        when (statusBarDisplayType) {
            StatusBarDisplayType.LIGHT -> setLightStatusBar()
            StatusBarDisplayType.DARK -> setDarkStatusBar()
        }
    }

    private fun setLightStatusBar() {
        activity?.let {
            var flags = it.window.decorView.systemUiVisibility

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                it.window.statusBarColor = resources.getColor(R.color.colorPrimary)
                it.window.decorView.systemUiVisibility = flags
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    it.window.statusBarColor = resources.getColor(android.R.color.transparent)
                }
            }
        }
    }

    private fun setDarkStatusBar() {
        activity?.let {
            var flags = it.window.decorView.systemUiVisibility

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                it.window.decorView.systemUiVisibility = flags
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.window.statusBarColor = resources.getColor(R.color.colorPrimary)
            }
        }
    }
}
