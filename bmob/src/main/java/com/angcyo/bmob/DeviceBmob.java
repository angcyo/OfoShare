package com.angcyo.bmob;

import com.angcyo.uiview.utils.Device;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by angcyo on 2017-04-17.
 * 设备信息
 */

public class DeviceBmob extends BmobObject {
    private String appVersionName;
    private String appVersionCode;
    private String osVersion;
    private String modelVendor;
    private String deviceHardware;
    private String cpu;
    private String memorySize;
    private String sdSize;

    private String userName;

    public DeviceBmob() {
        appVersionName = Device.appVersionName(RBmob.context);
        appVersionCode = Device.appVersionCode(RBmob.context);
        osVersion = Device.osVersion();
        modelVendor = Device.modelVendor();
        deviceHardware = Device.deviceHardware();
        cpu = Device.cpu();
        memorySize = Device.memorySize(RBmob.context);
        sdSize = Device.sdSize(RBmob.context);
    }

    public static void upload(String userName) {
        new DeviceBmob().setUserName(userName).save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {

                }
            }
        });
    }

    /**
     * 返回对象数量
     */
    public static void count(CountListener countListener) {
        BmobQuery<DeviceBmob> query = new BmobQuery<>();
        query.count(DeviceBmob.class, countListener);
    }

    public static void last(int sum, final cn.bmob.v3.listener.FindListener<DeviceBmob> listener) {
        BmobQuery<DeviceBmob> query = new BmobQuery<>();
        query.setSkip(sum - 1);
        query.findObjects(listener);
    }

    public static void last(final cn.bmob.v3.listener.FindListener<DeviceBmob> listener) {
        BmobQuery<DeviceBmob> query = new BmobQuery<>();
        query.order("-createdAt");//-表示逆序
        query.findObjects(listener);
    }

    public String getUserName() {
        return userName;
    }

    public DeviceBmob setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
