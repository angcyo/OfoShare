package com.angcyo.ofoshare.util;

import android.text.TextUtils;

import com.orhanobut.hawk.Hawk;

/**
 * Created by angcyo on 2017-04-17.
 */

public class Main {
    public static boolean isRegister() {
        return !TextUtils.isEmpty(userName());
    }

    public static void register(String userName) {
        Hawk.put("user_name", userName);
    }

    public static String userName() {
        return Hawk.get("user_name", "");
    }
}
