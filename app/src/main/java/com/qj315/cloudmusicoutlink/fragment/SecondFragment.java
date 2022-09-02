/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:SecondFragment.java
 * Date:2022/09/01 20:56:01
 */

package com.qj315.cloudmusicoutlink.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.qj315.cloudmusicoutlink.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {
    private boolean flag = false;

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.startUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = binding.phoneNumber.getText().toString();
                if (!flag) {
                    if (!phone.equals("") && phone.length() == 11) {
                        binding.webStart.setWebViewClient(new WebViewClient());
                        binding.webStart.loadUrl("http://www.a8zs.xyz/?sjh=" + phone);
                        flag = true;
//                  binding.webStart.destroy();
                        binding.phoneNumber.setText("");
                        binding.startUp.setText("停止");
                    } else {
                        Toast.makeText(getContext(), phone.equals("") ? "输入不能为空！" : "电话号码不正确！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    binding.webStart.destroy();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.webStart.destroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.webStart.destroy();
    }
}