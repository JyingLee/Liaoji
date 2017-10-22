package com.jying.rainbow.Module.Tuling;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.jying.rainbow.Adapter.ChatAdapter;
import com.jying.rainbow.Bean.ChatMessage;
import com.jying.rainbow.Bean.MyUser;
import com.jying.rainbow.Module.LoginActivity;
import com.jying.rainbow.Module.Setting.SettingActivity;
import com.jying.rainbow.Module.Xiaohua.XiaohuaActivity;
import com.jying.rainbow.R;
import com.jying.rainbow.Utils.Key;
import com.jying.rainbow.Utils.ToastUtils;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.turing.androidsdk.HttpRequestListener;
import com.turing.androidsdk.TuringManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jying on 2017/9/8.
 */

public class TulingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TulingContract.View {
    RecyclerView chatRecyclewView;
    ChatAdapter chatAdapter;
    Context con;
    Button send_bt;
    EditText send_edit;
    private TuringManager turingManager;
    List<ChatMessage> lists = new ArrayList<>();
    Toolbar toolbar;
    private long firstTime = 0;
    TulingContract.Presenter mPresenter;
    @BindView(R.id.nav_view)
    NavigationView nav_view;
    CircleImageView userIcon;
    ImageView weather_icon;
    TextView username;
    TextView weather_desc;
    TextView weather_neirong;
    String fankui;
    RelativeLayout shezhi_layout;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ChatMessage tulingChat = new ChatMessage((String) msg.obj, ChatMessage.TULING);
                    lists.add(tulingChat);
                    chatAdapter.notifyItemInserted(lists.size() - 1);
                    chatRecyclewView.scrollToPosition(lists.size() - 1);
                    break;
                case 1:
                    String url = msg.getData().getString("url");
                    String test = (String) msg.obj;
                    ChatMessage chat_url = new ChatMessage(test, ChatMessage.TULING_URL);
                    chat_url.setUrl(url);
                    lists.add(chat_url);
                    chatAdapter.notifyItemInserted(lists.size() - 1);
                    chatRecyclewView.scrollToPosition(lists.size() - 1);
                    break;
                case 2:
                    String weather_data = msg.getData().getString("weather_data");
                    String weather = msg.getData().getString("weather");
                    String weather_pic = msg.getData().getString("weather_pic");
                    if (weather_pic != null && weather != null && weather_pic != null) {
                        Glide.with(con).load(weather_pic).into(weather_icon);
                        weather_neirong.setText(weather_data);
                        weather_desc.setText(weather);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        con = TulingActivity.this;
        ButterKnife.bind(TulingActivity.this);
        mPresenter = new TulingPresenter(this);
        init();
        setUpdate();
        toolbar.setTitle("聊机");
        setSupportActionBar(toolbar);
        mPresenter.getWeatherData(handler);
        initTuling();
        dealData();
        initRecyclewView();
        ChatMessage init = new ChatMessage("只有帅的人才能向我提问,understand!?", ChatMessage.TULING);
        lists.add(init);
        chatAdapter.notifyItemInserted(lists.size() - 1);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) con);

        BmobQuery<MyUser> query = new BmobQuery<>();
        query.getObject(BmobUser.getCurrentUser(MyUser.class).getObjectId(), new QueryListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (e == null) {
                    Glide.with(con).load(user.getUserIcon().getUrl()).into(userIcon);
                    if (!user.getNickName().equals("")) {
                        username.setText(user.getNickName());
                    } else {
                        username.setText(user.getUsername());
                    }
                }else {
                    ToastUtils.showToast(con,"没网了?!");
                }
            }
        });
        shezhi_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(con,SettingActivity.class));
            }
        });
    }
    private void setUpdate() {
        PgyUpdateManager.setIsForced(false);
        PgyUpdateManager.register(TulingActivity.this, "",
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        Toast.makeText(TulingActivity.this,"又更新啦!",Toast.LENGTH_SHORT).show();
                        final AppBean appBean = getAppBeanFromString(result);
                        new AlertDialog.Builder(TulingActivity.this)
                                .setTitle("更新")
                                .setMessage("又是一个惊喜")
                                .setNegativeButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                startDownloadTask(
                                                        TulingActivity.this,
                                                        appBean.getDownloadURL());
                                            }
                                        }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        Toast.makeText(TulingActivity.this,"已经是最新版本",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void init() {
        chatRecyclewView = (RecyclerView) findViewById(R.id.chatRecyclewView);
        send_bt = (Button) findViewById(R.id.send_bt);
        send_edit = (EditText) findViewById(R.id.send_edit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        View headerLayout = nav_view.getHeaderView(0);
        userIcon = (CircleImageView) headerLayout.findViewById(R.id.userIcon);
        weather_icon = (ImageView) headerLayout.findViewById(R.id.weather_icon);
        username = (TextView) headerLayout.findViewById(R.id.username);
        weather_desc = (TextView) headerLayout.findViewById(R.id.weather_desc);
        weather_neirong = (TextView) headerLayout.findViewById(R.id.weather_neirong);
        shezhi_layout= (RelativeLayout) headerLayout.findViewById(R.id.shezhi_layout);
    }

    private void initTuling() {
        turingManager = new TuringManager(con, Key.TULINGAPI_KEY, Key.TULING_SECREY);
        turingManager.setHttpRequestListener(new HttpRequestListener() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Log.e("test", s);
                    try {
                        JSONObject result_obj = new JSONObject(s);
                        int code = result_obj.getInt("code");
                        if (code == 100000) {
                            if (result_obj.has("text")) {
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = result_obj.get("text");
                                handler.sendMessage(msg);
                            }
                        } else if (code == 200000) {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result_obj.get("text");
                            Bundle bundle = new Bundle();
                            bundle.putString("url", (String) result_obj.get("url"));
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        Log.e("test", "JSONException:" + e.getMessage());
                    }
                }
            }

            @Override
            public void onFail(int i, String s) {
                Log.e("test", "获取失败");
            }
        });
    }

    public void initRecyclewView() {
        chatAdapter = new ChatAdapter(this, lists);
        chatRecyclewView.setLayoutManager(new LinearLayoutManager(con, LinearLayoutManager.VERTICAL, false));
        chatRecyclewView.setAdapter(chatAdapter);
    }

    public void dealData() {
        send_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = send_edit.getText().toString();
                if (data.isEmpty()) {
                    Toast.makeText(con, "不说话怎么回答你?", Toast.LENGTH_SHORT).show();
                    return;
                }
                turingManager.requestTuring(data);
                send_edit.setText("");
                ChatMessage chatMessage = new ChatMessage(data, ChatMessage.ME);
                lists.add(chatMessage);
                chatAdapter.notifyItemInserted(lists.size() - 1);
                chatRecyclewView.scrollToPosition(lists.size() - 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            SharedPreferences sp = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.clear();
            ed.commit();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {                                                    //两次按键小于2秒时，退出应用
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_xiaohua) {
            startActivity(new Intent(con, XiaohuaActivity.class));
        }
//        else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        }
        else if (id == R.id.nav_setting) {
            startActivity(new Intent(con, SettingActivity.class));
        } else if (id == R.id.nav_fankui) {
            new MaterialDialog.Builder(con)
                    .title("个人反馈")
                    .negativeText("取消")
                    .positiveText("确认")
                    .input("", "", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            fankui = input.toString();
                        }
                    }).onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                }
            }).onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    ToastUtils.showToast(con, "反馈成功");
                }
            }).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setPresenter(TulingContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
