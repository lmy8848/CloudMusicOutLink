/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:LvDialog.java
 * Date:2022/09/15 18:34:15
 */

package com.qj315.cloudmusicoutlink.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ldoublem.loadingviewlib.view.LVBlazeWood;
import com.qj315.cloudmusicoutlink.R;

public class LvDialog extends Dialog {
    private LVBlazeWood blockLv;
    private TextView mMessageBox;

    public LvDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog);
        initView();
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        mMessageBox.setText("服务器连接中……");
        blockLv.startAnim(1000);
    }

    public void initView() {
        blockLv=findViewById(R.id.block_LV);
        mMessageBox=findViewById(R.id.message_box);
    }
}
