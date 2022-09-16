/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:Chat315Fragment.java
 * Date:2022/09/15 10:09:15
 */

package com.qj315.cloudmusicoutlink.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.qj315.cloudmusicoutlink.R;
import com.qj315.cloudmusicoutlink.adapter.SocketWeChatAdapter;
import com.qj315.cloudmusicoutlink.dialog.LvDialog;
import com.qj315.cloudmusicoutlink.tools.ToolsSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


public class Chat315Fragment extends Fragment {
    private static Socket socket;
    SocketWeChatAdapter socketWeChatAdapter;
    private View view;
    private ListView mMessageList;
    private TextInputEditText mInputMessageText;
    private RadioButton mSendBtn;
    private List<String> socketStreamMessage;
    private PrintWriter printWriter;
    private LvDialog dialogLoading;

    public Chat315Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat315, container, false);
        initView();

        socketStreamMessage = new LinkedList<>();
        socketWeChatAdapter = new SocketWeChatAdapter(socketStreamMessage, getContext());
//                    mMessageList.setAdapter(new SocketWeChatAdapter(socketStreamMessage, getContext()));
        mMessageList.setAdapter(socketWeChatAdapter);
//        mMessageList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Thread tag = new Thread(() -> {

                    socket = ToolsSet.ConnectionSocket("101.42.111.188", 9991);
                    try {
                        InputStream inputStream = socket.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String message = "";
                        dialogLoading.dismiss();
                        while ((message = bufferedReader.readLine()) != null) {
                            socketStreamMessage.add(message);
                            onUpdateUI(mMessageList, socketWeChatAdapter);
                            Log.i("TAG", "onCreateView: " + message);
//                            mMessageList.deferNotifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialogLoading = new LvDialog(requireActivity());
                                dialogLoading.show();
                            }
                        });
                        }catch (Exception err){
                            err.printStackTrace();
                        }
                        reading();
                        e.printStackTrace();
                    }
                });
                tag.start();
            }
        });
        mSendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String s = mInputMessageText.getText().toString();

                if (!s.equals("")) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            socketStreamMessage.add("@" + s + "@");
//                                            mMessageList.deferNotifyDataSetChanged();
                            onUpdateUI(mMessageList, socketWeChatAdapter);
                            mInputMessageText.setText("");
                        }
                    });

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (socket != null && socket.isConnected()) {
                                try {
                                    printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                                    printWriter.write(s + "\n");
                                    printWriter.flush();
                                    Log.i("TAG", "run: send Message!!!");
                                } catch (IOException e) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "发送失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    e.printStackTrace();
                                }
                            }

                        }
                    }).start();

                }
            }
        });
        return view;
    }

    public void initView() {
        mInputMessageText = view.findViewById(R.id.input_message_text);
        mMessageList = view.findViewById(R.id.message_list);
        mSendBtn = view.findViewById(R.id.send_btn);
        dialogLoading = new LvDialog(requireActivity());
        dialogLoading.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        dialogLoading.dismiss();
        Log.i("TAG", "onPause: Chat315Fragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TAG", "onResume:  Chat315Fragment");
    }

    public void onUpdateUI(ListView mMessageList, SocketWeChatAdapter socketWeChatAdapter) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessageList.deferNotifyDataSetChanged();
                socketWeChatAdapter.notifyDataSetChanged();
                mMessageList.setSelection(mMessageList.getCount()-1);
            }
        });
    }
    public void reading() {
        Thread tag = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!socket.isConnected()){
            socket = ToolsSet.ConnectionSocket("101.42.111.188", 9991);
            }
            try {
                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String message = "";
                dialogLoading.dismiss();
                while ((message = bufferedReader.readLine()) != null) {
                    socketStreamMessage.add(message);
                    onUpdateUI(mMessageList, socketWeChatAdapter);
                    Log.i("TAG", "onCreateView: " + message);
//                            mMessageList.deferNotifyDataSetChanged();
                }
            } catch (Exception e) {
//                dialogLoading.show();
                reading();
                e.printStackTrace();
            }
        });
        tag.start();


    }

}