package com.zap.marvygroup.ui

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.zap.marvygroup.R
import java.net.URLEncoder

class SalaryActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salary)
        webView = findViewById(R.id.webview)
        val webview = WebView(this)
        setContentView(webview)
        val url = "http://online.marvygroup.com/marvy_payroll/employee/view_employee_salary.php"
        val postData = "username=" + URLEncoder.encode("M008202", "UTF-8")
            .toString() + "&password=" + URLEncoder.encode("URV@123", "UTF-8")
        webview.postUrl(url, postData.toByteArray())

    }
    }