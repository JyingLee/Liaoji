package com.jying.rainbow.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jying on 2017/10/15.
 */

public class ToastUtils {

    public static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public static void showLog(String message){
        Log.e("============",message);
    }
}
