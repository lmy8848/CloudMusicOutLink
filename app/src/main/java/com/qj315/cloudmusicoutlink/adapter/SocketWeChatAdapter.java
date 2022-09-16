/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:SocketWeChatAdapter.java
 * Date:2022/09/15 10:55:15
 */

package com.qj315.cloudmusicoutlink.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qj315.cloudmusicoutlink.R;

import java.util.List;

public class SocketWeChatAdapter extends BaseAdapter {
    private List<String> messageList;
    private Context context;

    public SocketWeChatAdapter(List<String> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return messageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String item = (String) getItem(i);
        try {
            String substring = item.substring(item.indexOf("@") + 1, item.lastIndexOf("@"));
            Log.i("TAG@", "getView: " + substring);
            view = LayoutInflater.from(context).inflate(R.layout.self_item, null);
            TextView message = view.findViewById(R.id.self_message_text);
            message.setText(substring);
            return view;
        } catch (StringIndexOutOfBoundsException e) {
            view = LayoutInflater.from(context).inflate(R.layout.other_item, null);
            TextView message = view.findViewById(R.id.message_text);
            message.setText((String) getItem(i));
            Log.i("TAG~", "getView: " + e.getMessage());
            notifyDataSetChanged();
            return view;
        }
    }
}
