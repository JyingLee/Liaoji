package com.jying.rainbow.Adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jying.rainbow.Bean.XiaohuaBean;
import com.jying.rainbow.R;

import java.util.List;

/**
 * Created by Jying on 2017/10/15.
 */

public class XiaohuaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Handler handler;
    List<XiaohuaBean> lists;

    public XiaohuaAdapter(Handler handler, List<XiaohuaBean> lists) {
        this.handler = handler;
        this.lists = lists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new vh_xiaohua(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xiaohua, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        vh_xiaohua vh= (vh_xiaohua) holder;
        vh.item_xiaohua_neirong.setText("\u3000\u3000"+lists.get(position).getText());
        vh.item_xiaohua_title.setText("<"+lists.get(position).getTitle()+">");
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    private class vh_xiaohua extends RecyclerView.ViewHolder {
        TextView item_xiaohua_neirong;
        TextView item_xiaohua_title;
        public vh_xiaohua(View inflate) {
            super(inflate);
            item_xiaohua_neirong= (TextView) inflate.findViewById(R.id.item_xiaohua_neirong);
            item_xiaohua_title= (TextView) inflate.findViewById(R.id.item_xiaohua_title);
        }
    }
}
