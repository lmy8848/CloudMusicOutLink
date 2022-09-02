/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:AddMusicResourceActivity.java
 * Date:2022/08/04 19:42:04
 */

package com.qj315.cloudmusicoutlink.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.qj315.cloudmusicoutlink.MainActivity;
import com.qj315.cloudmusicoutlink.R;
import com.qj315.cloudmusicoutlink.bean.ResponseBean;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddMusicResourceActivity extends AppCompatActivity {

    private ImageView addBackHome;
    private TextInputEditText inputUrl;
    private Button submitUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music_resource);
        initView();
        addBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMusicResourceActivity.this, MainActivity.class));
                finish();
            }
        });

        submitUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = inputUrl.getText().toString();
                if (url.equals("")) {
                    AlertDialog.Builder dialog=new AlertDialog.Builder(AddMusicResourceActivity.this);
                    dialog.setTitle("提示").setMessage("URL不能为空！").setNegativeButton("了解", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://39.105.77.85:8848/api/out_link?url=" + url)
                                    .get()
                                    .addHeader("Content-Type", "application/json")
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    ResponseBean responseBean = new Gson().fromJson(response.body().string(), ResponseBean.class);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (responseBean.getCode() == 200) {
                                                inputUrl.setText("");
                                            } else {
                                                inputUrl.setText(responseBean.getToken());
                                            }
                                            @SuppressLint("ShowToast") Toast toast = Toast.makeText(AddMusicResourceActivity.this, responseBean.getToken(), Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }
                                    });
                                }
                            });
                        }
                    }).start();
                }
            }
        });


    }

    private void initView() {
        addBackHome = findViewById(R.id.add_back_home);
        inputUrl = findViewById(R.id.input_url);
        submitUrl = findViewById(R.id.submit_URl);
    }
}