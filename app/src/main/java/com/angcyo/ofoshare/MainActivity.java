package com.angcyo.ofoshare;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
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
import com.angcyo.uiview.net.Rx;
import com.angcyo.uiview.skin.SkinHelper;

import java.io.File;

import rx.functions.Action0;
import rx.functions.Action1;
import zlc.season.rxdownload.RxDownload;
import zlc.season.rxdownload.entity.DownloadStatus;

public class MainActivity extends UILayoutActivity {

    public static void installApp(Context context, File file) {
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    @Override
    protected String[] needPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };
    }

    @Override
    protected void onLoadView() {
        SkinHelper.setSkin(new MainSkin(this));

        if (Main.isRegister()) {
            DeviceBmob.upload(Main.userName());
            mLayout.replaceIView(new MainUIView(), new UIParam(false));
        } else {
            mLayout.replaceIView(new RegisterUIView(), new UIParam(false));
        }

        checkUpdate();
    }

    private void checkUpdate() {
        //UpdateBmob.test();

        UpdateBmob.checkUpdate(new UpdateBmob.UpdateListener() {
            @Override
            public void onUpdate(final UpdateBmob bmob) {
                L.e("onUpdate() -> 有更新:" + bmob.getUrl());
                RxDownload.getInstance()
                        .download(bmob.getUrl(), "ofoshare.apk",
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/ofoshare")
                        .compose(Rx.<DownloadStatus>transformer())
                        .last()
                        .doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ofoshare/ofoshare.apk";
                                final File file = new File(filePath);
                                //L.e("call() -> " + filePath + " " + file.exists());

                                if (file.exists()) {
                                    final UIDialog dialog = UIDialog.build();
                                    dialog.setDialogTitle("发现新版本:" + bmob.getVersionName())
                                            .setDialogContent(bmob.getDescribe())
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
        });
    }
}
