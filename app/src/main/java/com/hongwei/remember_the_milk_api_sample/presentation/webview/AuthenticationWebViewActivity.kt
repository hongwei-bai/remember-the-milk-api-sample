package com.hongwei.remember_the_milk_api_sample.presentation.webview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.webkit.WebView
import android.webkit.WebViewClient
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Constants.RESULT_CODE_SUCCESS
import com.hongwei.remember_the_milk_api_sample.R
import com.hongwei.remember_the_milk_api_sample.injection.component.DaggerActivityComponent
import com.hongwei.remember_the_milk_api_sample.injection.modules.ActivityModule
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_webview.*
import java.io.Serializable


class AuthenticationWebViewActivity : BaseActivity() {

    private val urlRequest: UrlRequest by lazy {
        intent.extras.getSerializable(KEY_URL_REQUEST) as UrlRequest
    }

    companion object {
        private const val KEY_URL_REQUEST = "url_request"

        fun newIntent(context: Context, urlRequest: UrlRequest): Intent {
            return Intent(context, AuthenticationWebViewActivity::class.java).apply {
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

            /*
             * + POSITIVE
             * + login success:
             *      https://www.rememberthemilk.com/login/success/
             *
             * + stay login in:
             *      https://www.rememberthemilk.com/services/auth/?api_key=28c5974a50066a0b200bec226e91179f&format=json&frob=45b26c35fbbe6f7b625dfc285df01122ad5ff735&perms=delete&api_sig=7902987903e6a07f4f0acda08cf11a20
             *
             * - NAGITIVE
             * - login failure
             *      https://www.rememberthemilk.com/login/?tryagain=1
             *
             * - Need login
             *      https://www.rememberthemilk.com/login/?api=api_key%3D28c5974a50066a0b200bec226e91179f%26format%3Djson%26frob%3D7849d8bd896eb891419e10b9d0f4cb070b0663de%26perms%3Ddelete%26api_sig%3Db532ed9e6ce4e02a329a795084009040
             *
             */
            override fun onPageFinished(view: WebView, url: String) {
                progress_bar.visibility = INVISIBLE
                swipe_refresh_layout.isRefreshing = false
                supportActionBar?.subtitle = url

                if (url.contains("/login/success/") || (url.contains("/services/auth/"))) {
                    setResult(RESULT_CODE_SUCCESS)
                    finish()
                }
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
