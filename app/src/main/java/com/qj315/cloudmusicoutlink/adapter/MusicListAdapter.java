/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:MusicListAdapter.java
 * Date:2022/08/04 19:49:04
 */

package com.qj315.cloudmusicoutlink.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qj315.cloudmusicoutlink.MusicList;
import com.qj315.cloudmusicoutlink.R;

import java.util.List;

public class MusicListAdapter extends BaseAdapter {
    private Context context;
    private List<MusicList> list;
    private TextView listId,musicName,musicSrc;


    public MusicListAdapter(Context context, List<MusicList> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MusicList getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.list_item,null);
        MusicList musicList=list.get(i);
        initView(view);
        listId.setText(i+1+"");
        musicName.append(musicList.getMusic_name());
        musicSrc.setText(musicList.getMusic_url());
        return view;
    }

    public void initView(View view) {
        listId=view.findViewById(R.id.list_id);
        musicName=view.findViewById(R.id.music_name);
        musicSrc=view.findViewById(R.id.music_src);
    }


}
