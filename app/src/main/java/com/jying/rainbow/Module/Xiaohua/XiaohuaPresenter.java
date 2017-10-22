package com.jying.rainbow.Module.Xiaohua;

import android.os.Handler;

import com.jying.rainbow.Bean.XiaohuaBean;
import com.jying.rainbow.Utils.Key;
import com.show.api.ShowApiRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Jying on 2017/10/15.
 */

public class XiaohuaPresenter implements XiaohuaContract.Presenter {
    XiaohuaContract.View mView;

    public XiaohuaPresenter(XiaohuaContract.View mView){
        this.mView=mView;
    }
    @Override
    public void start() {

    }

    @Override
    public void getData(final Handler handler, final int page, final List<XiaohuaBean>lists) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String res=new ShowApiRequest(Key.XIAOHUA_API,Key.YIYUANID,Key.YIYUAN_SECRET)
                        .addTextPara("page", String.valueOf(page))
                        .addTextPara("maxResult","20")
                        .post();
                try {
                    JSONObject jsonObject=new JSONObject(res);
                    JSONObject showapi_res_body=new JSONObject(jsonObject.getString("showapi_res_body"));
                    JSONArray jsonArray=showapi_res_body.getJSONArray("contentlist");
                    for (int i=0;i<jsonArray.length();i++){
                        XiaohuaBean xiaohuaBean=new XiaohuaBean();
                        JSONObject body=jsonArray.getJSONObject(i);
                        String text=body.getString("text");
                        String title=body.getString("title");
                        xiaohuaBean.setText(text);
                        xiaohuaBean.setTitle(title);
                        lists.add(xiaohuaBean);
                        handler.sendEmptyMessage(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
