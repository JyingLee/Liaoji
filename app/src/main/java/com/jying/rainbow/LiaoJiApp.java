package com.jying.rainbow;

import android.app.Application;
import android.content.Context;

import com.jying.rainbow.Module.Chat.base.UniversalImageLoader;
import com.jying.rainbow.Utils.Key;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

/**
 * Created by Jying on 2017/10/15.
 */

public class LiaoJiApp extends Application {
    Context context;
    private static LiaoJiApp INSTANCE;

    public static LiaoJiApp INSTANCE() {
        return INSTANCE;
    }

    private void setInstance(LiaoJiApp app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(LiaoJiApp a) {
        LiaoJiApp.INSTANCE = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bmob.initialize(context, Key.BMOB_ID);
        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new NewMessageHandler(this));
        }
        com.orhanobut.logger.Logger.init("BmobNewIMDemo");
        UniversalImageLoader.initImageLoader(this);
    }

    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
