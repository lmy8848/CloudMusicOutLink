/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:WebSiteFragment.java
 * Date:2022/09/02 13:00:02
 */

package com.qj315.cloudmusicoutlink.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qj315.cloudmusicoutlink.R;
public class WebSiteFragment extends Fragment {
    private View view;
    private WebView mWebSite;
    private FloatingActionButton mWebGoBack;

    public WebSiteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_web_site, container, false);
        initView();
        mWebSite.setWebViewClient(new WebViewClient());
        mWebSite.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebSite.getSettings().setJavaScriptEnabled(true);
        mWebSite.getSettings().setDomStorageEnabled(true);
        mWebSite.getSettings().setAllowFileAccess(true);
        mWebSite.getSettings().setAllowContentAccess(true);
        mWebSite.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebSite.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebSite.loadUrl("file:///android_asset/docs/index.html");
        mWebGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebSite.goBack();
            }
        });

        return view;
    }

    private void initView() {
        mWebSite=view.findViewById(R.id.web_site);
        mWebGoBack = view.findViewById(R.id.web_go_back);
    }
}