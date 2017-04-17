package com.angcyo.bmob;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by angcyo on 2017-04-17.
 */

public class UserBmob {
    private String userName;

    public UserBmob() {
    }

    public static void isUserExist(String userName, FindListener listener) {
        BmobQuery<UserBmob> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("userName", userName).findObjects(listener);
    }
}
