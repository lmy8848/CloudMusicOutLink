/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:PlayerActivity.java
 * Date:2022/07/27 17:17:27
 */

package com.qj315.cloudmusicoutlink.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.qj315.cloudmusicoutlink.MainActivity;
import com.qj315.cloudmusicoutlink.MusicList;
import com.qj315.cloudmusicoutlink.PlayMusicService;
import com.qj315.cloudmusicoutlink.PlayMusicTest;
import com.qj315.cloudmusicoutlink.R;

public class PlayerActivity extends AppCompatActivity implements PlayMusicTest {

    private static PlayMusicService.MyBinder binder;
    ServiceConnection serviceConnection;
    private TextView musicTitle;
    private ImageView imageView,backHome;
    private SeekBar seekBar;
    private ImageButton Previousbtn, playbtn, nextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                binder = (PlayMusicService.MyBinder) iBinder;
                Log.i("TAG", "onServiceConnected: ");
                Intent intentUrl = getIntent();
                String music_url = intentUrl.getStringExtra("music_url");
                String music_name = intentUrl.getStringExtra("music_name");
                int subscript = intentUrl.getIntExtra("p", 0);
                binder.getService().setP(subscript);
                if (binder == null) {
                    Log.i("TAG", "onServiceConnected:绑定为空！ ");
                } else {
                    if (music_url != null) {
                        binder.getService().ready(music_url);
                        binder.getService().play();
                        setNotify();
                    }
                    runOnUiThread(() -> {
                        Log.i("TAG", "music_name: " + music_name);
                        musicTitle.append(music_name);
                        seekBar.setEnabled(true);
                        Previousbtn.setEnabled(true);
                        playbtn.setEnabled(true);
                        nextbtn.setEnabled(true);
                    });

                }

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.i("TAG", "onServiceDisconnected: ");
            }

            @Override
            public void onBindingDied(ComponentName name) {

            }

            @Override
            public void onNullBinding(ComponentName name) {

            }

        };
        Intent intent = new Intent(PlayerActivity.this, PlayMusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean play = binder.getService().isPlay();
                Log.i("TAG", "onClick: " + play);
                if (play) {
                    binder.getService().pause();
                    playbtn.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    binder.getService().player();
                    playbtn.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        Previousbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                MusicList musicList = binder.getService().PreviousPlay();
                musicTitle.setText("歌曲名称" + musicList.getMusic_name());
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                MusicList musicList = binder.getService().NextPlay();
                musicTitle.setText("歌曲名称" + musicList.getMusic_name());
                playbtn.setImageResource(android.R.drawable.ic_media_pause);
            }
        });
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(PlayerActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    protected void setNotify() {
        binder.getService().setPlayMusicTest(this);
    }

    @Override
    public void playMusic(int CurrentPosition) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(CurrentPosition);
            }
        });
    }

    @Override
    public void setPosition(int Position) {

    }

    public void initView() {
        musicTitle = findViewById(R.id.music_title);
        imageView = findViewById(R.id.music_img);
        seekBar = findViewById(R.id.seekBar);
        Previousbtn = findViewById(R.id.Previous);
        playbtn = findViewById(R.id.music_play);
        nextbtn = findViewById(R.id.next_music);
        backHome=findViewById(R.id.back_home);
        seekBar.setEnabled(false);
        Previousbtn.setEnabled(false);
        playbtn.setEnabled(false);
        nextbtn.setEnabled(false);

    }


}