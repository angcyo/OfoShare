package com.angcyo.ofoshare;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.angcyo.bmob.UpdateBmob;
import com.angcyo.library.utils.L;
import com.angcyo.ofoshare.uiview.MainUIView;
import com.angcyo.ofoshare.uiview.RegisterUIView;
import com.angcyo.ofoshare.util.Main;
import com.angcyo.uiview.RCrashHandler;
import com.angcyo.uiview.Root;
import com.angcyo.uiview.base.UILayoutActivity;
import com.angcyo.uiview.container.UIParam;
import com.angcyo.uiview.dialog.UIDialog;
import com.angcyo.uiview.github.utilcode.utils.AppUtils;
import com.angcyo.uiview.github.utilcode.utils.ClipboardUtils;
import com.angcyo.uiview.github.utilcode.utils.CmdUtil;
import com.angcyo.uiview.github.utilcode.utils.FileUtils;
import com.angcyo.uiview.github.utilcode.utils.NetworkUtils;
import com.angcyo.uiview.net.Rx;
import com.angcyo.uiview.receiver.NetworkStateReceiver;
import com.angcyo.uiview.skin.SkinHelper;
import com.angcyo.uiview.utils.T_;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.orhanobut.hawk.Hawk;

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
    protected void onLoadView(Intent intent) {
        SkinHelper.setSkin(new MainSkin(this));

        if (Main.isRegister()) {
            //DeviceBmob.upload(Main.userName());
            mLayout.replaceIView(new MainUIView(), new UIParam(false));
//            mLayout.replaceIView(new RegisterUIView(), new UIParam(false));
        } else {
            mLayout.replaceIView(new RegisterUIView(), new UIParam(false));
        }

        checkUpdate();

        checkCrash();
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
        L.e("开始检查更新");
        UpdateBmob.checkUpdate(new UpdateBmob.UpdateListener() {
            @Override
            public void onUpdate(final UpdateBmob bmob) {
                L.e("onUpdate() -> 有更新:" + bmob.getUrl());
//                if (BuildConfig.DEBUG) {
//                    showInstallDialog(bmob, null);
//                    return;
//                }
                final String fileName = getFileName(bmob.getVersionName());

                String folder = getFolder();
                File file = new File(folder + fileName);

                if (file.exists()) {
                    showInstallDialog(bmob, file);
                    isChecking = false;
                    return;
                }

                L.e("开始下载:" + bmob.getUrl() + " :" + folder + fileName + ".temp");

                RxDownload.getInstance()
                        .download(bmob.getUrl(), fileName + ".temp", folder)
                        .compose(Rx.<DownloadStatus>transformer())
                        .last()
                        .doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                File targetFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                        + "/" + Root.APP_FOLDER + "/" + fileName);
                                String filePath = targetFile.getAbsolutePath() + ".temp";
                                final File file = new File(filePath);
                                L.e("下载完成 -> " + filePath + " " + file.exists());

                                if (file.exists()) {
                                    if (file.renameTo(targetFile)) {
                                        showInstallDialog(bmob, targetFile);
                                    }
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
                        AppUtils.installApp(MainActivity.this, file);
                    }
                })
                .setCanCancel(bmob.getForce() != 1)
                .showDialog(mLayout);
    }

    private void checkCrash() {
        if (Hawk.get(RCrashHandler.KEY_IS_CRASH, false)) {
            //异常退出了
            UIDialog.build()
                    .setDialogTitle("发生了什么啊^_^")
                    .setDialogContent(Hawk.get(RCrashHandler.KEY_CRASH_MESSAGE, "-"))
                    .setOkText("粘贴给作者?")
                    .setCancelText("加入QQ群")
                    .setCancelListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            joinQQGroup("TO1ybOZnKQHSLcUlwsVfOt6KQMGLmuAW");
                        }
                    })
                    .setOkListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                ClipboardUtils.copyText(FileUtils.readFile2String(Hawk.get(RCrashHandler.KEY_CRASH_FILE, "-"), "utf8"));

                                String qq = "664738095";
                                if (CmdUtil.checkApkExist(MainActivity.this, "com.tencent.mobileqq")) {
                                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    T_.error("您没有安装腾讯QQ");
                                }
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .showDialog(mLayout);
        }

        Hawk.put(RCrashHandler.KEY_IS_CRASH, false);
    }

    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            T_.error("您没有安装腾讯QQ");
            return false;
        }
    }
}
