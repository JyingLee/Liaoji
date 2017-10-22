package com.jying.rainbow;

import android.app.Application;
import android.content.Context;

import com.jying.rainbow.Utils.Key;

import cn.bmob.v3.Bmob;

/**
 * Created by Jying on 2017/10/15.
 */

public class LiaoJiApp extends Application {
    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        Bmob.initialize(context, Key.BMOB_ID);
    }
}
