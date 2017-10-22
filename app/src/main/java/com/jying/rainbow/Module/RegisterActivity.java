package com.jying.rainbow.Module;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jying.rainbow.Bean.MyUser;
import com.jying.rainbow.Database.DatabaseHelper;
import com.jying.rainbow.R;
import com.jying.rainbow.Utils.ToastUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Jying on 2017/9/9.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText et_username;
    TextInputEditText et_password;
    TextInputEditText et_confirm;
    Button bt_register;
    DatabaseHelper helper;
    SQLiteDatabase db;
    ImageButton register_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();
    }

    private void init() {
        et_username = (TextInputEditText) findViewById(R.id.et_register_username);
        et_password = (TextInputEditText) findViewById(R.id.et_register_password);
        et_confirm = (TextInputEditText) findViewById(R.id.et_register_confirm);
        bt_register = (Button) findViewById(R.id.bt_register_register);
        register_back = (ImageButton) findViewById(R.id.register_back);
        bt_register.setOnClickListener(this);
        register_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register_register:
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String confirm_password = et_confirm.getText().toString();
                if (username.isEmpty()) {
                    et_username.setError("用户名不能为空");
                    return;
                }
                if (password.isEmpty()) {
                    et_password.setError("密码不能为空");
                    return;
                }
                if (confirm_password.isEmpty()) {
                    et_confirm.setError("密码不能为空");
                    return;
                }
                if (!et_password.getText().toString().equals(et_confirm.getText().toString())) {
                    Toast.makeText(this, "两次密码输入不一样", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!username.isEmpty() && !password.isEmpty() && !confirm_password.isEmpty()) {
//                    Cursor cursor = db.rawQuery("select *from user where username=?", new String[]{username});
//                    boolean isExist = cursor.moveToNext();
//                    if (isExist) {
//                        Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
//                        et_username.setText("");
//                    } else {
//                        db.execSQL("insert into user(username,password) values(?,?)", new Object[]{username, password});
//                        db.close();
//                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(this, TulingActivity.class));
//                        finish();
//                        LoginActivity.instance.finish();
//                    }
                    MyUser user = new MyUser();
                    user.setUsername(username);
                    user.setPassword(confirm_password);
                    user.signUp(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser user, BmobException e) {
                            if (e == null) {
                                ToastUtils.showToast(RegisterActivity.this, "注册成功");
                                LoginActivity.instance.finish();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            }
                            else {
                                ToastUtils.showToast(RegisterActivity.this,"用户名已存在");
                            }
                        }
                    });
                }
                break;
            case R.id.register_back:
                finish();
                break;
        }
    }
}
