package com.jying.rainbow.Module.Chat.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.jying.rainbow.Module.Chat.base.BaseActivity;
import com.jying.rainbow.Module.Chat.bean.User;
import com.jying.rainbow.Module.Chat.model.UserModel;
import com.jying.rainbow.R;


/**启动界面
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = UserModel.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(LoginActivity.class,null,true);
                }else{
                    startActivity(MainActivity.class,null,true);
                }
            }
        },1000);

    }
}
