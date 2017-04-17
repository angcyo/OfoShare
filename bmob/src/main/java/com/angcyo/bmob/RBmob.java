package com.angcyo.bmob;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by angcyo on 2017-04-17.
 * Bmob 版本:3.5.3
 */

public class RBmob {
    public static Application context;

    public static void init(Context context) {
        Bmob.initialize(context, "6588be35d60a2c1569fa1ee961930d1b");
        RBmob.context = (Application) context;
    }
}
