package com.jying.rainbow.Module.Chat.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jying.rainbow.Module.Chat.base.ParentWithNaviActivity;
import com.jying.rainbow.Module.Chat.event.FinishEvent;
import com.jying.rainbow.Module.Chat.model.BaseModel;
import com.jying.rainbow.Module.Chat.model.UserModel;
import com.jying.rainbow.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * 注册界面
 *
 */
public class RegisterActivity extends ParentWithNaviActivity {

    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_register)
    Button btn_register;

    @BindView(R.id.et_password_again)
    EditText et_password_again;

    @Override
    protected String title() {
        return "注册";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register123);
        initNaviView();
    }

    /**
     * 注册
     *
     * @param view
     */
    @OnClick(R.id.btn_register)
    public void onRegisterClick(View view) {
        UserModel.getInstance().register(et_username.getText().toString(), et_password.getText().toString(), et_password_again.getText().toString(), new LogInListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    EventBus.getDefault().post(new FinishEvent());
                    startActivity(MainActivity.class, null, true);
                } else {
                    if (e.getErrorCode() == BaseModel.CODE_NOT_EQUAL) {
                        et_password_again.setText("");
                    }
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

}
