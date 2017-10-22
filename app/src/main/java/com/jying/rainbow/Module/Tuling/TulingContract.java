package com.jying.rainbow.Module.Tuling;

import android.os.Handler;

import com.jying.rainbow.Base.BasePresenter;
import com.jying.rainbow.Base.BaseView;

/**
 * Created by Jying on 2017/10/15.
 */

public class TulingContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getWeatherData(Handler handler);
    }
}
