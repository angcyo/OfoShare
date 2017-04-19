package com.angcyo.ofoshare;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.angcyo.bmob.DeviceBmob;
import com.angcyo.bmob.UpdateBmob;
import com.angcyo.library.utils.L;
import com.angcyo.ofoshare.uiview.MainUIView;
import com.angcyo.ofoshare.uiview.RegisterUIView;
import com.angcyo.ofoshare.util.Main;
import com.angcyo.uiview.base.UILayoutActivity;
import com.angcyo.uiview.container.UIParam;
import com.angcyo.uiview.dialog.UIDialog;
import com.angcyo.uiview.github.utilcode.utils.AppUtils;
import com.angcyo.uiview.github.utilcode.utils.NetworkUtils;
import com.angcyo.uiview.net.Rx;
import com.angcyo.uiview.receiver.NetworkStateReceiver;
import com.angcyo.uiview.skin.SkinHelper;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.io.File;

import rx.functions.Action0;
import rx.functions.Action1;
import zlc.season.rxdownload.RxDownload;
import zlc.season.rxdownload.entity.DownloadStatus;

public class MainActivity extends UILayoutActivity {

    boolean isChecking = false;

    @Override
    protected String[] needPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    @Subscribe(tags = {@Tag("update")})
    public void onEvent(String update) {
        checkUpdate();
    }

    @Override
    protected void onLoadView() {
        SkinHelper.setSkin(new MainSkin(this));

        if (Main.isRegister()) {
            DeviceBmob.upload(Main.userName());
            mLayout.replaceIView(new MainUIView(), new UIParam(false));
//            mLayout.replaceIView(new RegisterUIView(), new UIParam(false));
        } else {
            mLayout.replaceIView(new RegisterUIView(), new UIParam(false));
        }

        checkUpdate();
    }

    private void checkUpdate() {
        //UpdateBmob.test();

        if (NetworkStateReceiver.getNetType() != NetworkUtils.NetworkType.NETWORK_WIFI) {
            return;
        }

        if (isChecking) {
            return;
        }

        isChecking = true;
        UpdateBmob.checkUpdate(new UpdateBmob.UpdateListener() {
            @Override
            public void onUpdate(final UpdateBmob bmob) {
                L.e("onUpdate() -> 有更新:" + bmob.getUrl());
//                if (BuildConfig.DEBUG) {
//                    showInstallDialog(bmob, null);
//                    return;
//                }
                File file = new File(getFolder() + getFileName(bmob.getVersionName()));

                if (file.exists()) {
                    AppUtils.installApp(MainActivity.this, file);
                    isChecking = false;
                    return;
                }

                RxDownload.getInstance()
                        .download(bmob.getUrl(), getFileName(bmob.getVersionName()), getFolder())
                        .compose(Rx.<DownloadStatus>transformer())
                        .last()
                        .doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ofoshare/ofoshare.apk";
                                final File file = new File(filePath);
                                //L.e("call() -> " + filePath + " " + file.exists());

                                if (file.exists()) {
                                    showInstallDialog(bmob, file);
                                }

                                isChecking = false;
                            }
                        })
                        .subscribe(new Action1<DownloadStatus>() {
                            @Override
                            public void call(DownloadStatus downloadStatus) {

                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });

            }

            @Override
            public void onLast(UpdateBmob bmob) {

            }
        });
    }

    private String getFolder() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/ofoshare/";
    }

    private String getFileName(String versionName) {
        return "ofoshare" + versionName + ".apk";
    }

    private void showInstallDialog(UpdateBmob bmob, final File file) {
        final UIDialog dialog = UIDialog.build();

        StringBuilder builder = new StringBuilder();
        String describe = bmob.getDescribe();
        if (!TextUtils.isEmpty(describe)) {
            String[] split = describe.split("\\\\n");
            for (int i = 0; i < 1; i++) {
                for (String s : split) {
                    builder.append(s.trim());
                    builder.append("\n");
                }
            }
        }
        dialog.setDialogTitle("发现新版本:" + bmob.getVersionName())
                .setDialogContent(builder.toString())
                .setCancelText(bmob.getForce() == 1 ? "" : "下次再说")
                .setOkText("立即安装")
                .setOkListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setCanCancel(true);
                        AppUtils.installApp(MainActivity.this, file);
                    }
                })
                .setCanCancel(bmob.getForce() != 1)
                .showDialog(mLayout);
    }
}
