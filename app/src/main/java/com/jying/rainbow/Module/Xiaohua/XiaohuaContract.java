package com.jying.rainbow.Module.Xiaohua;

import android.os.Handler;

import com.jying.rainbow.Base.BasePresenter;
import com.jying.rainbow.Base.BaseView;
import com.jying.rainbow.Bean.XiaohuaBean;

import java.util.List;

/**
 * Created by Jying on 2017/10/15.
 */

public class XiaohuaContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getData(Handler handler,int page,List<XiaohuaBean> lists);
    }
}
