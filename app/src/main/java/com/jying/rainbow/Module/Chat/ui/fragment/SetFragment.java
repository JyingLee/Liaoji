package com.jying.rainbow.Module.Chat.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jying.rainbow.Module.Chat.base.ParentWithNaviFragment;
import com.jying.rainbow.Module.Chat.bean.User;
import com.jying.rainbow.Module.Chat.model.UserModel;
import com.jying.rainbow.Module.Chat.ui.LoginActivity;
import com.jying.rainbow.Module.Chat.ui.MainActivity;
import com.jying.rainbow.Module.Chat.ui.UserInfoActivity;
import com.jying.rainbow.Module.Tuling.TulingActivity;
import com.jying.rainbow.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;

/**
 * 设置
 */
public class SetFragment extends ParentWithNaviFragment {

    @BindView(R.id.tv_set_name)
    TextView tv_set_name;

    @BindView(R.id.layout_info)
    RelativeLayout layout_info;


    @Override
    protected String title() {
        return "设置";
    }

    public static SetFragment newInstance() {
        SetFragment fragment = new SetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_set, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        String username = UserModel.getInstance().getCurrentUser().getUsername();
        tv_set_name.setText(TextUtils.isEmpty(username) ? "" : username);
        return rootView;
    }

    @OnClick(R.id.layout_info)
    public void onInfoClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("u", BmobUser.getCurrentUser(User.class));
        startActivity(UserInfoActivity.class, bundle);
    }

    @OnClick(R.id.btn_logout)
    public void onLogoutClick(View view) {
        UserModel.getInstance().logout();
        //TODO 连接：3.2、退出登录需要断开与IM服务器的连接
        BmobIM.getInstance().disConnect();
        getActivity().finish();
        startActivity(LoginActivity.class, null);
    }

    @OnClick(R.id.switch_robot)
    public void onSwitchRobot(View view) {
        startActivity(TulingActivity.class, null);
        MainActivity.instance.finish();
    }
}
