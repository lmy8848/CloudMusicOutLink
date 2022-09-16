/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:MainActivity.java
 * Date:2022/07/27 17:33:27
 */

package com.qj315.cloudmusicoutlink;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.qj315.cloudmusicoutlink.activity.SettingActivity;
import com.qj315.cloudmusicoutlink.databinding.ActivityMainBinding;
import com.qj315.cloudmusicoutlink.tools.ToolsSet;

import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        BottomNavigationView navView = binding.bottomItem;
        SharedPreferences sharedPreferences=getSharedPreferences("switch", Activity.MODE_PRIVATE);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homes, R.id.tools_set,R.id.web_site_test,R.id.socket_send)
                .build();
//        try {
//            Process ls = Runtime.getRuntime().exec("ls");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setItemIconTintList(null);
        if (sharedPreferences.getBoolean("swdata",true)){
        runOnUiThread(()-> ToolsSet.ShowDownLoad(MainActivity.this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ToolsSet.addHistory();
    }
}