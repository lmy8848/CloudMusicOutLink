/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:SettingActivity.java
 * Date:2022/08/04 19:49:04
 */

package com.qj315.cloudmusicoutlink.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qj315.cloudmusicoutlink.MainActivity;
import com.qj315.cloudmusicoutlink.R;
import com.qj315.cloudmusicoutlink.bean.ListViewItemBean;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import constant.UiType;
import listener.Md5CheckResultListener;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

public class SettingActivity extends AppCompatActivity {

    private ListView settingList;
    private ImageView goBack;
    private List<ListViewItemBean> beanList;
    private SharedPreferences sp;
    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        UpdateAppUtils.init(this);
        beanList=new ArrayList<>();
        addItemObj();
        sp=getSharedPreferences("switch", Activity.MODE_PRIVATE);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                finish();
            }
        });
        settingList.setAdapter(new BaseAdapter() {
            ImageView updateIcon,gotoIcon;
            SwitchButton switchButton;
            TextView title,switch_hint;
            @Override
            public int getCount() {
                return beanList.size();
            }

            @Override
            public Object getItem(int i) {
                return beanList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @SuppressLint("ViewHolder")
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view=LayoutInflater.from(SettingActivity.this).inflate(R.layout.setting_item,null);
                interview(view);
                updateIcon.setImageResource(beanList.get(i).getDrawable());
                gotoIcon.setImageResource(beanList.get(i).getListIconbtn());
                title.setText(beanList.get(i).getTitle());
                if (beanList.get(i).getTitle().equals("检查更新")){
                    switchButton.setVisibility(View.VISIBLE);
                    switch_hint.setVisibility(View.VISIBLE);
                    switchButton.setChecked(sp.getBoolean("swdata",true));
                }
                switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                        SharedPreferences.Editor edit =sp.edit();
                        edit.putBoolean("swdata",isChecked);
                        edit.apply();
                    }
                });
                return view;
            }
            public void interview(View view){
                updateIcon=view.findViewById(R.id.setting_img);
                gotoIcon=view.findViewById(R.id.setting_btn);
                title=view.findViewById(R.id.setting_title);
                switch_hint=view.findViewById(R.id.switch_hint);
                switchButton=view.findViewById(R.id.switch_button);
            }
        });
        settingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListViewItemBean item = (ListViewItemBean) adapterView.getAdapter().getItem(i);
                if (item.getTitle().equals("检查更新")){
                    UpdateConfig updateConfig = new UpdateConfig();
                    updateConfig.setNotifyImgRes(R.drawable.vector_drawable_cloudmusicgreen);
//                    updateConfig.getServerVersionCode()

                    updateConfig.setThisTimeShow(true);
                    updateConfig.setCheckWifi(true);
                    updateConfig.setNeedCheckMd5(false);
                    updateConfig.setAlwaysShowDownLoadDialog(true);
                    UiConfig uiConfig = new UiConfig();
                    uiConfig.setUiType(UiType.PLENTIFUL);
                    UpdateAppUtils
                            .getInstance()
                            .apkUrl("http://110.42.174.137/app/app-release.apk")
                            .updateTitle("发现新版本")
                            .updateContent("UI更新,以及新的下拉刷新功能,期待您的探索与反馈！")
                            .uiConfig(uiConfig)
                            .updateConfig(updateConfig).setMd5CheckResultListener(new Md5CheckResultListener() {
                        @Override
                        public void onResult(boolean b) {

                        }
                    }).setUpdateDownloadListener(new UpdateDownloadListener() {
                        @Override
                        public void onStart() {
                            Toast.makeText(SettingActivity.this,"开始下载",Toast.LENGTH_SHORT).show();
                            Log.i("TAG", "onItemClick: "+updateConfig.getServerVersionCode());
                            Log.i("TAG", "onStart:Download ");
                        }

                        @Override
                        public void onDownload(int i) {

                        }

                        @Override
                        public void onFinish() {
                            Toast.makeText(SettingActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(@NonNull Throwable throwable) {

                            Toast.makeText(SettingActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).update();
                }else if (item.getTitle().equals("添加音乐")){
                    Intent intent=new Intent(SettingActivity.this, AddMusicResourceActivity.class);
                    startActivity(intent);
                }else if (item.getTitle().equals("京东Cookie获取")){
                    Intent intent=new Intent(SettingActivity.this, JDCookieGet.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void initView() {
        settingList=findViewById(R.id.setting_list);
        goBack=findViewById(R.id.go_back);
    }
    public void addItemObj(){
        ListViewItemBean setting_update=new ListViewItemBean();
        setting_update.setDrawable(R.drawable.vector_drawable_update_32_);
        setting_update.setTitle("检查更新");
        setting_update.setListIconbtn(R.drawable.vector_drawable_goto);
        beanList.add(setting_update);
        ListViewItemBean setting_add_music=new ListViewItemBean();
        setting_add_music.setDrawable(R.drawable.vector_drawable_add_music_icon);
        setting_add_music.setTitle("添加音乐");
        setting_add_music.setListIconbtn(R.drawable.vector_drawable_goto);
        beanList.add(setting_add_music);
        ListViewItemBean setting_add_JD=new ListViewItemBean();
        setting_add_JD.setDrawable(R.drawable.vector_drawable_jdicon_01);
        setting_add_JD.setTitle("京东Cookie获取");
        setting_add_JD.setListIconbtn(R.drawable.vector_drawable_goto);
        beanList.add(setting_add_JD);

    }
}