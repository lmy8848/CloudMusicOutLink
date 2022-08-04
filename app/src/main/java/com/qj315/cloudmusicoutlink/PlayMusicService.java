/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:PlayMusicService.java
 * Date:2022/08/03 14:36:03
 */

package com.qj315.cloudmusicoutlink;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PlayMusicService extends Service {
    private final static MediaPlayer mediaPlayer = new MediaPlayer();
    private static List<MusicList> musicLists;
    private final MyBinder myBinder = new MyBinder();
    PlayMusicTest playMusicTest;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int P = 0;

    public PlayMusicService() {
    }

    public void setMusicLists(List<MusicList> musicLists) {
        PlayMusicService.musicLists = musicLists;
    }

    public void setP(int p) {
        P = p;
    }

    public void setPlayMusicTest(PlayMusicTest playMusicTest) {
        this.playMusicTest = playMusicTest;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public void ready(String musicUrl) {


        try {
            mediaPlayer.reset();
            // todo: set looping unused
            mediaPlayer.setLooping(false);
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(musicUrl));
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.i("TAG", "ready: ");
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                NextPlay();
                Log.i("TAG", "onCompletion: NextPlay();");
            }
        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        if (mediaPlayer.isPlaying()) {
//                            playMusicTest.playMusic((int) ((mediaPlayer.getCurrentPosition() * 1.0f / mediaPlayer.getDuration()) * 10000));
//                            Log.i("TAG", "run:*-*-*-*-*- ");
//                        }
//                        Thread.sleep(250);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
        //定时任务重复执行
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                try {
                    if (mediaPlayer.isPlaying()) {
                        playMusicTest.playMusic((int) ((mediaPlayer.getCurrentPosition() * 1.0f / mediaPlayer.getDuration()) * 10000));
                        Log.i("TAG", "run:*-*-*-*-*- ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask,0,500);

    }

    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public void play() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
//            mediaPlayer.start();//开始播放

        }
    }

    public void player() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.reset();//停止播放
        }
    }

    public boolean isPlay() {
        return mediaPlayer.isPlaying();
    }

    public MusicList NextPlay() {
        if (P < musicLists.size() - 1) {
            ready(musicLists.get(++P).getMusic_url());
            play();
            Log.i("TAG", "NextPlay: " + P);
            return musicLists.get(P);
        } else {
            P = 0;
            ready(musicLists.get(P).getMusic_url());
            play();
            Log.i("TAG", "NextPlay: " + P);
            return musicLists.get(P);
        }
    }

    public MusicList PreviousPlay() {
        if (P > 0) {
            ready(musicLists.get(--P).getMusic_url());
            play();
            Log.i("TAG", "PreviousPlay: " + P);
            return musicLists.get(P);
        } else {
            P = musicLists.size() - 1;
            ready(musicLists.get(P).getMusic_url());
            play();
            Log.i("TAG", "PreviousPlay: " + P);
            return musicLists.get(P);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onCreate();
    }

    public class MyBinder extends Binder {
        public PlayMusicService getService() {
            return PlayMusicService.this;
        }
    }


}