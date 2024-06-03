package com.cliyo.sphynx

import android.content.Context
import android.webkit.CookieManager
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    private lateinit var myWebView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview)

        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.setWebViewClient(WebViewClient())
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(myWebView, true)
        myWebView.loadUrl("http://100.119.19.90")
        val webSettings: WebSettings = myWebView.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

    }

    class mywebClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
//    class WebAppInterface(private val mContext: Context) {
//        @JavascriptInterface
//        fun showToast(toast: String) {
//            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}