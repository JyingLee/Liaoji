package com.jying.rainbow.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jying.rainbow.Bean.ChatMessage;
import com.jying.rainbow.Bean.MyUser;
import com.jying.rainbow.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Jying on 2017/9/9.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context con;
    List<ChatMessage> lists;
    private String name;
    private String url;

    public ChatAdapter(Context con, List<ChatMessage> lists) {
        this.con = con;
        this.lists = lists;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new chat_vh(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof chat_vh) {
            chat_vh vh = (chat_vh) holder;
            if (lists.get(position).getType() == ChatMessage.TULING) {
                vh.tuling_layout.setVisibility(View.VISIBLE);
                vh.me_layout.setVisibility(View.GONE);
                vh.tuling_url_layout.setVisibility(View.GONE);
                vh.tuling_tv.setText(lists.get(position).getText());
            } else if (lists.get(position).getType() == ChatMessage.ME) {
                queryUserData();//查询用户信息
                vh.tuling_layout.setVisibility(View.GONE);
                vh.me_layout.setVisibility(View.VISIBLE);
                vh.tuling_url_layout.setVisibility(View.GONE);
                vh.me_tv.setText(lists.get(position).getText());
                if (url != null && !url.equals("")) {
                    Glide.with(con).load(url).into(vh.me_image);
                }
                if (name != null && !name.equals("")) {
                    vh.me_name_tv.setText(name);
                }
            } else if (lists.get(position).getType() == ChatMessage.TULING_URL) {
                vh.tuling_layout.setVisibility(View.GONE);
                vh.me_layout.setVisibility(View.GONE);
                vh.tuling_url_layout.setVisibility(View.VISIBLE);
                vh.tuling_tv1.setText(lists.get(position).getText());
                Log.e("test", lists.get(position).getUrl());
                Glide.with(con).load(lists.get(position).getUrl()).error(R.mipmap.shithub).into(vh.tuling_url_image);
            }
        }
    }

    private void queryUserData() {
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.getObject(BmobUser.getCurrentUser(MyUser.class).getObjectId(), new QueryListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (e == null) {
                    if (!user.getUserIcon().getUrl().equals("")) {
                        url = user.getUserIcon().getUrl();
                    }
                    if (!user.getNickName().equals("")) {
                        name = user.getNickName();
                    } else {
                        name = user.getUsername();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }


    private class chat_vh extends RecyclerView.ViewHolder {
        TextView tuling_tv;
        TextView me_tv;
        RelativeLayout tuling_layout;
        RelativeLayout me_layout;
        RelativeLayout tuling_url_layout;
        TextView tuling_tv1;
        ImageView tuling_url_image;
        CircleImageView me_image;
        TextView me_name_tv;

        public chat_vh(View inflate) {
            super(inflate);
            tuling_url_layout = (RelativeLayout) inflate.findViewById(R.id.tuling_url_layout);
            tuling_tv = (TextView) inflate.findViewById(R.id.tuling_tv);
            me_tv = (TextView) inflate.findViewById(R.id.me_tv);
            tuling_layout = (RelativeLayout) inflate.findViewById(R.id.tuling_layout);
            me_layout = (RelativeLayout) inflate.findViewById(R.id.me_layout);
            tuling_tv1 = (TextView) inflate.findViewById(R.id.tuling_tv1);
            tuling_url_image = (ImageView) inflate.findViewById(R.id.tuling_url_image);
            me_image = (CircleImageView) inflate.findViewById(R.id.me_image);
            me_name_tv = (TextView) inflate.findViewById(R.id.me_name_tv);
        }
    }
}
