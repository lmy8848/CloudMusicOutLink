/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:FirstFragment.java
 * Date:2022/08/31 15:03:31
 */

package com.qj315.cloudmusicoutlink.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.qj315.cloudmusicoutlink.MusicList;
import com.qj315.cloudmusicoutlink.PlayMusicService;
import com.qj315.cloudmusicoutlink.R;
import com.qj315.cloudmusicoutlink.activity.PlayerActivity;
import com.qj315.cloudmusicoutlink.adapter.MusicListAdapter;
import com.qj315.cloudmusicoutlink.databinding.FragmentFirstBinding;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class FirstFragment extends Fragment{

    private FragmentFirstBinding binding;
    private List<MusicList> musicLists;

    private PlayMusicService.MyBinder binder;



    private  ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder= (PlayMusicService.MyBinder) iBinder;
            Log.i("TAG", "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("TAG", "onServiceDisconnected: ");
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentFirstBinding.inflate(inflater, container, false);

        Intent intent=new Intent(getActivity(),PlayMusicService.class);
        requireActivity().bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
        reFresh();
        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.musicList.setAdapter(new MusicListAdapter(getContext(),musicLists));
//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        binding.musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MusicList item =(MusicList)adapterView.getAdapter().getItem(i);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(getActivity(), PlayerActivity.class);
                        intent.putExtra("music_url",item.getMusic_url());
                        Log.i("TAG", "run: "+item.getMusic_name());
                        intent.putExtra("music_name",item.getMusic_name());
                        intent.putExtra("p",i);
                        startActivity(intent);
//                        binder.getService().ready(item.getMusic_url());
//                        binder.getService().play();
                    }
                });
                Log.i("TAG", "onItemClick: "+item.getId());
            }
        });
        binding.pullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
               binding.pullToRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reFresh();
                        binding.pullToRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void reFresh(){
        new Thread(() -> {
            musicLists = new ArrayList<>();
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://39.105.77.85:8848/api/get/music/list")
                    .get()
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String string = response.body().string();
                    Log.i("TAG", "onResponse: "+string);
                    try {
                        JSONArray jsonArray = new JSONArray(string);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            musicLists.add(new Gson().fromJson(jsonArray.get(i).toString(), MusicList.class));
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.musicList.setAdapter(new MusicListAdapter(getContext(),musicLists));
                                binding.musicList.deferNotifyDataSetChanged();
                                binder.getService().setMusicLists(musicLists);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }).start();
    }

}