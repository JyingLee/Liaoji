package com.jying.rainbow.Module.Xiaohua;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jying.rainbow.Adapter.XiaohuaAdapter;
import com.jying.rainbow.Bean.XiaohuaBean;
import com.jying.rainbow.R;
import com.jying.rainbow.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jying on 2017/10/15.
 */

public class XiaohuaActivity extends AppCompatActivity implements XiaohuaContract.View {
    Context context;
    XiaohuaContract.Presenter mPresenter;
    @BindView(R.id.recyclewview_xiaohua)
    RecyclerView recyclerView;
    XiaohuaAdapter adapter;
    List<XiaohuaBean>lists=new ArrayList<>();
    @BindView(R.id.xiaohua_refresh)
    SwipeRefreshLayout refresh;
    private int page=1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    refresh.setRefreshing(false);
                    break;
                case 1:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaohua);
        context = XiaohuaActivity.this;
        ButterKnife.bind((Activity) context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("笑话大全");
        toolbar.setNavigationIcon(R.drawable.back_black);
        setSupportActionBar(toolbar);
        mPresenter = new XiaohuaPresenter(this);
        initRecyclewView();
        initRefresh();
        mPresenter.getData(handler,page,lists);
    }

    private void initRefresh() {
        refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToastUtils.showToast(context,"刷新");
                page++;
                lists.clear();
                mPresenter.getData(handler,page,lists);
            }
        });
    }

    private void initRecyclewView() {
        adapter = new XiaohuaAdapter(handler,lists);
        LinearLayoutManager layoutmanager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutmanager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPresenter(XiaohuaContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
