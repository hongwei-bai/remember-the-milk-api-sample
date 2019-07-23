package com.hongwei.remember_the_milk_api_sample.presentation.webview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.webkit.WebView
import android.webkit.WebViewClient
import com.hongwei.remember_the_milk_api_sample.R
import com.hongwei.remember_the_milk_api_sample.injection.component.DaggerActivityComponent
import com.hongwei.remember_the_milk_api_sample.injection.modules.ActivityModule
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_webview.*
import java.io.Serializable


class RegisterWebViewActivity : BaseActivity() {

    private val urlRequest: UrlRequest by lazy {
        intent.extras.getSerializable(KEY_URL_REQUEST) as UrlRequest
    }

    companion object {
        private const val KEY_URL_REQUEST = "url_request"

        fun newIntent(context: Context, urlRequest: UrlRequest): Intent {
            return Intent(context, RegisterWebViewActivity::class.java).apply {
                putExtra(KEY_URL_REQUEST, urlRequest)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        setToolbar()
        configureWebView()
        hookupSwipeRefreshLayout()
        webview.loadUrl(urlRequest.url)
    }

    private fun hookupSwipeRefreshLayout() {
        swipe_refresh_layout.setColorSchemeResources(R.color.colorAccent)

        swipe_refresh_layout.setOnRefreshListener {

        }
    }

    private fun configureWebView() {
        webview.settings.javaScriptEnabled = true
        webview.settings.allowContentAccess = false
        webview.settings.allowFileAccess = false

        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progress_bar.visibility = VISIBLE
            }

            override fun onPageFinished(view: WebView, url: String) {
                progress_bar.visibility = INVISIBLE
                swipe_refresh_layout.isRefreshing = false
                supportActionBar?.subtitle = url
            }
        }
    }

    private fun setToolbar() {
        setBackActionBarButton(findViewById(R.id.toolbar_webview))

        supportActionBar?.let {
            it.title = urlRequest.title
            it.subtitle = urlRequest.url
            it.setHomeAsUpIndicator(R.drawable.ic_close)
        }
    }

    override fun inject() {
        DaggerActivityComponent.builder()
            .applicationComponent(getAppComponent())
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
    }

    class UrlRequest(val title: String,
                     val url: String) : Serializable
}
