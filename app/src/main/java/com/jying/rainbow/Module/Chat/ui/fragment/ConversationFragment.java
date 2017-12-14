package com.jying.rainbow.Module.Chat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.jying.rainbow.Bean.MyUser;
import com.jying.rainbow.Module.Chat.adapter.ConversationAdapter;
import com.jying.rainbow.Module.Chat.adapter.OnRecyclerViewListener;
import com.jying.rainbow.Module.Chat.adapter.base.IMutlipleItem;
import com.jying.rainbow.Module.Chat.base.ParentWithNaviActivity;
import com.jying.rainbow.Module.Chat.base.ParentWithNaviFragment;
import com.jying.rainbow.Module.Chat.bean.Conversation;
import com.jying.rainbow.Module.Chat.bean.NewFriendConversation;
import com.jying.rainbow.Module.Chat.bean.PrivateConversation;
import com.jying.rainbow.Module.Chat.db.NewFriend;
import com.jying.rainbow.Module.Chat.db.NewFriendManager;
import com.jying.rainbow.Module.Chat.event.RefreshEvent;
import com.jying.rainbow.Module.Chat.ui.MainActivity;
import com.jying.rainbow.Module.Chat.ui.SearchUserActivity;
import com.jying.rainbow.Module.Setting.SettingActivity;
import com.jying.rainbow.Module.Tuling.TulingActivity;
import com.jying.rainbow.Module.Xiaohua.XiaohuaActivity;
import com.jying.rainbow.R;
import com.jying.rainbow.Utils.Key;
import com.jying.rainbow.Utils.ToastUtils;
import com.show.api.ShowApiRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 会话界面
 */
public class ConversationFragment extends ParentWithNaviFragment implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.rc_view)
    RecyclerView rc_view;
    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    ConversationAdapter adapter;
    LinearLayoutManager layoutManager;
    @BindView(R.id.ll_root)
    DrawerLayout drawer;
    @BindView(R.id.nav_view_chat)
    NavigationView navigationView;
    private String fankui;
    CircleImageView userIcon;
    ImageView weather_icon;
    TextView username, weather_desc, weather_neirong;
    RelativeLayout shezhi_layout;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    String weather_data = msg.getData().getString("weather_data");
                    String weather = msg.getData().getString("weather");
                    String weather_pic = msg.getData().getString("weather_pic");
                    if (!weather_pic.equals("") && !weather.equals("") && !weather_pic.equals("")) {
                        Glide.with(getContext()).load(weather_pic).into(weather_icon);
                        weather_neirong.setText(weather_data);
                        weather_desc.setText(weather);
                    }
                    break;
            }
        }
    };

    @Override
    protected String title() {
        return "会话";
    }

    @Override
    public Object right() {
        return R.drawable.base_action_bar_add_bg_selector;
    }

    @Override
    public Object left() {
        return R.drawable.santiao;
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {
                drawer.openDrawer(GravityCompat.START);
            }

            @Override
            public void clickRight() {
                startActivity(SearchUserActivity.class, null);
            }
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_conversation, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        //单一布局
        IMutlipleItem<Conversation> mutlipleItem = new IMutlipleItem<Conversation>() {

            @Override
            public int getItemViewType(int postion, Conversation c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_conversation;
            }

            @Override
            public int getItemCount(List<Conversation> list) {
                return list.size();
            }
        };
        initView();
        navigationView.setNavigationItemSelectedListener(this);
        initUserData();
        getWeatherData(handler);

        adapter = new ConversationAdapter(getActivity(), mutlipleItem, null);
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        setListener();
        return rootView;
    }

    private void getWeatherData(final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String res = new ShowApiRequest(Key.WEATHER_API, Key.YIYUANID, Key.YIYUAN_SECRET)
                        .addTextPara("area", "广东省花都区")
                        .post();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONObject showapi_res_body = new JSONObject(jsonObject.getString("showapi_res_body"));
                    String time = showapi_res_body.getString("time");
                    String year = time.substring(0, 4);
                    String month = time.substring(4, 6);
                    String day = time.substring(6, 8);
                    JSONObject now = new JSONObject(showapi_res_body.getString("now"));
                    String weather_pic = now.getString("weather_pic");
                    String wind_direction = now.getString("wind_direction");
                    String weather = now.getString("weather");
                    String temperature = now.getString("temperature");
                    String weather_data = year + "/" + month + "/" + day + "  " + wind_direction + "  " + temperature + "℃";
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("weather_data", weather_data);
                    bundle.putString("weather", weather);
                    bundle.putString("weather_pic", weather_pic);
                    msg.setData(bundle);
                    msg.what = 2;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    void initUserData() {
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.getObject(BmobUser.getCurrentUser(MyUser.class).getObjectId(), new QueryListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (e == null) {
                    Glide.with(getActivity()).load(user.getUserIcon().getUrl()).into(userIcon);
                    if (!user.getNickName().equals("")) {
                        username.setText(user.getNickName());
                    } else {
                        username.setText(user.getUsername());
                    }
                } else {
                }
            }
        });
    }

    private void initView() {
        View headerLayout = navigationView.getHeaderView(0);
        userIcon = (CircleImageView) headerLayout.findViewById(R.id.userIcon);
        weather_icon = (ImageView) headerLayout.findViewById(R.id.weather_icon);
        username = (TextView) headerLayout.findViewById(R.id.username);
        weather_desc = (TextView) headerLayout.findViewById(R.id.weather_desc);
        weather_neirong = (TextView) headerLayout.findViewById(R.id.weather_neirong);
        shezhi_layout = (RelativeLayout) headerLayout.findViewById(R.id.shezhi_layout);
    }


    private void setListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                query();
            }
        });
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                adapter.getItem(position).onClick(getActivity());
            }

            @Override
            public boolean onItemLongClick(int position) {
                adapter.getItem(position).onLongClick(getActivity());
                adapter.remove(position);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        query();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 查询本地会话
     */
    public void query() {
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
        sw_refresh.setRefreshing(false);
    }

    /**
     * 获取会话列表的数据：增加新朋友会话
     *
     * @return
     */
    private List<Conversation> getConversations() {
        //添加会话
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.clear();
        //TODO 会话：4.2、查询全部会话
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if (list != null && list.size() > 0) {
            for (BmobIMConversation item : list) {
                switch (item.getConversationType()) {
                    case 1://私聊
                        conversationList.add(new PrivateConversation(item));
                        break;
                    default:
                        break;
                }
            }
        }
        //添加新朋友会话-获取好友请求表中最新一条记录
        List<NewFriend> friends = NewFriendManager.getInstance(getActivity()).getAllNewFriend();
        if (friends != null && friends.size() > 0) {
            conversationList.add(new NewFriendConversation(friends.get(0)));
        }
        //重新排序
        Collections.sort(conversationList);
        return conversationList;
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        log("---会话页接收到自定义消息---");
        //因为新增`新朋友`这种会话类型
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        //重新刷新列表
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }

    /**
     * 注册消息接收事件
     *
     * @param event 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     *              2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        //重新获取本地消息并刷新列表
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_xiaohua) {
            startActivity(new Intent(getContext(), XiaohuaActivity.class));
        } else if (id == R.id.nav_robot) {
            startActivity(new Intent(getContext(), TulingActivity.class));
            MainActivity.instance.finish();
        }
// else if (id == R.id.nav_manage) {
//
//        }
        else if (id == R.id.nav_setting) {
            startActivity(new Intent(getContext(), SettingActivity.class));
        } else if (id == R.id.nav_fankui) {
            new MaterialDialog.Builder(getContext())
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
                    ToastUtils.showToast(getContext(), "反馈成功");
                }
            }).show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
