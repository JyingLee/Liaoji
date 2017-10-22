package com.jying.rainbow.Module.Tuling;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jying.rainbow.Utils.Key;
import com.show.api.ShowApiRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jying on 2017/10/15.
 */

public class TulingPresenter implements TulingContract.Presenter {
    TulingContract.View mView;

    public TulingPresenter(TulingContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void start() {

    }

    @Override
    public void getWeatherData(final Handler handler) {
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
                    String weather_data=year+"/"+month+"/"+day+"  "+wind_direction+"  "+temperature+"℃";
                    Message msg=handler.obtainMessage();
                    Bundle bundle=new Bundle();
                    bundle.putString("weather_data",weather_data);
                    bundle.putString("weather",weather);
                    bundle.putString("weather_pic",weather_pic);
                    msg.setData(bundle);
                    msg.what=2;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
