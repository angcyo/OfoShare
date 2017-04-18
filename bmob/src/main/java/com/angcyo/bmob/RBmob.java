package com.angcyo.bmob;

import android.app.Application;
import android.content.Context;

import com.angcyo.uiview.utils.T_;

import cn.bmob.v3.Bmob;

/**
 * Created by angcyo on 2017-04-17.
 * Bmob 版本:3.5.3
 */

public class RBmob {
    public static Application context;

    public static void init(Context context) {
        try {
            Bmob.initialize(context, "6588be35d60a2c1569fa1ee961930d1b");
        } catch (Exception e) {
            e.printStackTrace();
            T_.show("不支持的设备.");
        }
        RBmob.context = (Application) context;
    }
}
