/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:ToolsSet.java
 * Date:2022/09/03 18:29:03
 */

package com.qj315.cloudmusicoutlink.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.qj315.cloudmusicoutlink.R;
import com.qj315.cloudmusicoutlink.bean.Bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;

import constant.UiType;
import listener.Md5CheckResultListener;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import update.UpdateAppUtils;

public class ToolsSet {
    /**
     *
     * @param context
     */
    private static Socket socket;
    public static void ShowDownLoad(Context context){
        UpdateAppUtils.init(context);
        UpdateConfig updateConfig = new UpdateConfig();
        updateConfig.setNotifyImgRes(R.drawable.vector_drawable_cloudmusicgreen);
//                    updateConfig.getServerVersionCode()
        updateConfig.setThisTimeShow(true);
        updateConfig.setCheckWifi(true);
        updateConfig.setNeedCheckMd5(false);
        updateConfig.setAlwaysShowDownLoadDialog(true);
        UiConfig uiConfig = new UiConfig();
        uiConfig.setUpdateLogoImgRes(R.drawable.vector_drawable_cloudmusicgreen);
        uiConfig.setUiType(UiType.PLENTIFUL);
        UpdateAppUtils
                .getInstance()
                .apkUrl("http://110.42.174.137/app/app-release.apk")
                .updateTitle("发现新版本")
                .updateContent("UI更新,用户IP记录,因为持续测试更新中,故没有验证版本更新,期待您的探索与反馈！\n http://39.105.77.85 可以评论区留言")
                .uiConfig(uiConfig)
                .updateConfig(updateConfig).setMd5CheckResultListener(new Md5CheckResultListener() {
            @Override
            public void onResult(boolean b) {

            }
        }).setUpdateDownloadListener(new UpdateDownloadListener() {
            @Override
            public void onStart() {
                Toast.makeText(context,"开始下载",Toast.LENGTH_SHORT).show();
                Log.i("TAG", "onItemClick: "+updateConfig.getServerVersionCode());
                Log.i("TAG", "onStart:Download ");
            }

            @Override
            public void onDownload(int i) {

            }

            @Override
            public void onFinish() {
                Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

                Toast.makeText(context, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).update();
    }
    public static  void addHistory(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("https://ip.cn/api/index?ip=&type=0");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                InputStreamReader inputStreamReader = null;
                try {
                    assert url != null;
                    inputStreamReader = new InputStreamReader(url.openStream());

                    Thread.sleep(2000);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
                if (inputStreamReader != null) {
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();

                    while (true) {

                        try {
                            String in;
                            if ((in = bufferedReader.readLine()) != null) {
                                // System.out.println(in);
                                stringBuilder.append(in);
                                Gson gson = new Gson();
                                Bean bean = gson.fromJson(stringBuilder.toString(), Bean.class);
                                addOHttp(bean.getIp(),bean.getAddress());
                            } else {
                                in = bufferedReader.readLine();
                                bufferedReader.close();
                                inputStreamReader.close();
                                break;
                            }
                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }
                    // System.out.println(stringBuilder.toString());
                } else {
                    Log.i("TAG", "run:IP获取时网络异常 ");
                }


            }
        }).start();
    }
    private static void addOHttp(String ip, String address){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("ip",ip)
                .addFormDataPart("address",address)
                .build();
        Request request = new Request.Builder()
                .url("http://39.105.77.85:8848/api/add/location")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("TAG", "onResponse: "+response.body().string());
            }
        });

    }

    public static Socket ConnectionSocket(String ip,int port){
//        if (socket!=null){
//            if (socket.isConnected()){
//                return socket;
//            }else {
//                try {
//                socket=new Socket(ip,port);
//                    socket.setKeepAlive(true);
//                    return socket;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        socket=null;
        try {
            socket=new Socket(ip,port);
            socket.setKeepAlive(true);
//            socket.setSoTimeout(20000);

            return socket;
        } catch (IOException e) {
            e.printStackTrace();
            return socket;
        }
    }
}
