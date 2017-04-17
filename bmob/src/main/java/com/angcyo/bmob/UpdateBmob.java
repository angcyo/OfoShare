package com.angcyo.bmob;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by angcyo on 2017-04-17.
 */

public class UpdateBmob extends BmobObject {
    private String versionName;
    private int versionCode;
    private String url;
    private int force;

    public UpdateBmob() {
    }

    public static void checkUpdate(final UpdateListener listener) {
        BmobQuery<UpdateBmob> query = new BmobQuery<>();
        query.findObjects(new FindListener<UpdateBmob>() {
            @Override
            public void done(List<UpdateBmob> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        listener.onUpdate(list.get(list.size() - 1));
                    }
                }
            }
        });
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
    }
}
