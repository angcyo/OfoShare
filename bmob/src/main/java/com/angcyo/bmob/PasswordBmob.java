package com.angcyo.bmob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2017/04/18 11:21
 * 修改人员：Robi
 * 修改时间：2017/04/18 11:21
 * 修改备注：
 * Version: 1.0.0
 */
public class PasswordBmob extends BmobObject {
    private String password;
    private String number;
    private String username;

    public PasswordBmob() {
    }

    public PasswordBmob(String password, String number, String username) {
        this.password = password;
        this.number = number;
        this.username = username;
    }

    public static void upload(PasswordBmob passwordBmob, SaveListener listener) {
        passwordBmob.save(listener);
    }

    public static void find(String number, final PasswordBmob.FindListener listener) {
        BmobQuery<PasswordBmob> query = new BmobQuery<>();
        query.addWhereEqualTo("number", number)
                .findObjects(new cn.bmob.v3.listener.FindListener<PasswordBmob>() {
                    @Override
                    public void done(List<PasswordBmob> list, BmobException e) {
                        List<String> strings = new ArrayList<>();
                        if (e == null) {
                            Collections.reverse(list);//逆序
                            for (PasswordBmob bmob : list) {
                                strings.add(bmob.getPassword());
                            }
                        }
                        listener.onFind(strings);
                    }
                });
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public interface FindListener {
        void onFind(List<String> passwords);
    }
}
