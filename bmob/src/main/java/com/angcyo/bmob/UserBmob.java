package com.angcyo.bmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by angcyo on 2017-04-17.
 */

public class UserBmob extends BmobObject {
    private String userName;

    public UserBmob() {
    }

    public static void isUserExist(String userName, FindListener listener) {
        BmobQuery<UserBmob> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("userName", userName).findObjects(listener);
    }

    public static void upload(String userName, SaveListener listener) {
        new UserBmob().setUserName(userName).save(listener);
    }

    public static void find(final cn.bmob.v3.listener.FindListener<UserBmob> listener) {
        BmobQuery<UserBmob> query = new BmobQuery<>();
        query.findObjects(listener);
    }

    public static void findLast(final cn.bmob.v3.listener.FindListener<UserBmob> listener) {
        BmobQuery<UserBmob> query = new BmobQuery<>();
        query.order("-createdAt");//-表示逆序
        query.findObjects(listener);
    }

    public static void last(int sum, final cn.bmob.v3.listener.FindListener<UserBmob> listener) {
        BmobQuery<UserBmob> query = new BmobQuery<>();
        query.setSkip(sum - 1);
        query.findObjects(listener);
    }

    /**
     * 返回对象数量
     */
    public static void count(CountListener countListener) {
        BmobQuery<UserBmob> query = new BmobQuery<>();
        query.count(UserBmob.class, countListener);
    }

    public String getUserName() {
        return userName;
    }

    public UserBmob setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
