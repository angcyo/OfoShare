package com.angcyo.bmob;

import com.angcyo.uiview.utils.Device;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by angcyo on 2017-04-17.
 */

public class UpdateBmob extends BmobObject {
    private String versionName;
    private int versionCode;
    private String url;
    private int force;
    private String describe;

    public UpdateBmob() {
    }

    public static void test() {
        UpdateBmob updateBmob = new UpdateBmob();
        updateBmob.setUrl("https://raw.githubusercontent.com/angcyo/OfoShare/master/app.app");
        updateBmob.setVersionCode(10000000);
        updateBmob.setVersionName("Test_2017-04-17");
        updateBmob.setDescribe("用来测试的数据");
        updateBmob.setForce(1);
        updateBmob.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {

                }
            }
        });
    }

    public static void checkUpdate(final UpdateListener listener) {
        BmobQuery<UpdateBmob> query = new BmobQuery<>();
        query.order("-createdAt");//-表示逆序
        query.setLimit(1);
        query.findObjects(new FindListener<UpdateBmob>() {
            @Override
            public void done(List<UpdateBmob> list, BmobException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        UpdateBmob updateBmob = list.get(0);
                        listener.onLast(updateBmob);
                        try {
                            if (updateBmob.getVersionCode() > Integer.valueOf(Device.appVersionCode(RBmob.context))) {
                                listener.onUpdate(updateBmob);
                            }
                        } catch (NumberFormatException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public interface UpdateListener {
        void onUpdate(UpdateBmob bmob);

        void onLast(UpdateBmob bmob);
    }
}
