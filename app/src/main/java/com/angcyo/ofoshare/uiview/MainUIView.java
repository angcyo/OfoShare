package com.angcyo.ofoshare.uiview;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.TextView;

import com.angcyo.bmob.PasswordBmob;
import com.angcyo.bmob.UpdateBmob;
import com.angcyo.bmob.UserBmob;
import com.angcyo.ofoshare.R;
import com.angcyo.ofoshare.util.Main;
import com.angcyo.uiview.base.Item;
import com.angcyo.uiview.base.SingleItem;
import com.angcyo.uiview.dialog.UILoading;
import com.angcyo.uiview.github.utilcode.utils.SpannableStringUtils;
import com.angcyo.uiview.model.TitleBarPattern;
import com.angcyo.uiview.recycler.RBaseViewHolder;
import com.angcyo.uiview.skin.SkinHelper;
import com.angcyo.uiview.utils.Device;
import com.angcyo.uiview.utils.RUtils;
import com.angcyo.uiview.utils.T_;
import com.angcyo.uiview.utils.TimeUtil;
import com.angcyo.uiview.widget.ExEditText;
import com.angcyo.uiview.widget.RTextView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.functions.Action1;

/**
 * Created by angcyo on 2017-04-17.
 */

public class MainUIView extends BaseItemUIView {
    public static int MAX_NUM_LENGTH = 4;//编号限制最小长度
    private TextView mVersionNameView;
    private RTextView mDataCountView;
    private String mLastBy;
    private String mUserName;

    @Override
    protected TitleBarPattern getTitleBar() {
        return super.getTitleBar().setShowBackImageView(false).setTitleString("Ofo Share : " + Main.userName());
    }

    @Override
    public int getDefaultBackgroundColor() {
        return SkinHelper.getSkin().getThemeColor();
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_view_main;
    }

    @Override
    protected void createItems(List<SingleItem> items) {
        items.add(new SingleItem() {

            @Override
            public void onBindView(final RBaseViewHolder holder, int posInData, Item dataBean) {
                mVersionNameView = holder.tv(R.id.version_name_view);
                mVersionNameView.setText("Cv:" + Device.appVersionName(mActivity));

                mDataCountView = holder.v(R.id.data_count_view);

                final TextInputLayout inputLayout = holder.v(R.id.edit_text_layout);
                final ExEditText exEditText = holder.v(R.id.edit_text);

                holder.v(R.id.add_view).setBackground(SkinHelper.getSkin().getThemeMaskBackgroundRoundSelector(Color.RED));
                holder.v(R.id.find_view).setBackground(SkinHelper.getSkin().getThemeMaskBackgroundRoundSelector(Color.RED));

                holder.v(R.id.add_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (exEditText.isEmpty() || exEditText.length() < MAX_NUM_LENGTH) {
                            exEditText.requestFocus();
                            inputLayout.setError("认真一点.");
                        } else {
                            startIView(new AddDialog(exEditText.string()));
                        }
                    }
                });
                holder.v(R.id.find_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (exEditText.isEmpty() || exEditText.length() < MAX_NUM_LENGTH) {
                            exEditText.requestFocus();
                            inputLayout.setError("臣妾做不到啊.");
                        } else {
                            holder.tv(R.id.result_view).setText("查询中:" + TimeUtil.getBeijingNowTime("HH:mm:ss:SSS"));
                            UILoading.show2(mILayout);
                            PasswordBmob.find(exEditText.string(), new PasswordBmob.FindListener() {
                                @Override
                                public void onFind(List<String> passwords) {
                                    UILoading.hide();
                                    if (passwords.isEmpty()) {
                                        holder.tv(R.id.result_view).setText("没有找到 " + exEditText.string() + " 相关的密码, 赶快去共享吧!");
                                    } else {
                                        SpannableStringUtils.Builder builder = SpannableStringUtils.getBuilder(
                                                "找到" + exEditText.string() + "的结果" + passwords.size() + "个:\n");

                                        for (int i = 0; i < 1; i++) {
                                            for (String s : passwords) {
                                                builder.append(s).setTextSize((int) (scaledDensity() * 14));
                                                builder.append("\n");
                                            }
                                        }
                                        holder.tv(R.id.result_view).setText(builder.create());
                                    }
                                }
                            });
                        }
                    }
                });

                holder.v(R.id.san_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new RxPermissions(mActivity)
                                .request(Manifest.permission.CAMERA)
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean aBoolean) {
                                        if (aBoolean) {
                                            startIView(new RScanUIView(new Action1<String>() {
                                                @Override
                                                public void call(String s) {
                                                    Pattern compile = Pattern.compile("\\d{4,}");
                                                    Matcher matcher = compile.matcher(s);
                                                    if (matcher.find()) {
                                                        exEditText.setText(matcher.group(matcher.groupCount()));
                                                    } else {
                                                        exEditText.setText(s);
                                                    }
                                                }
                                            }));
                                        } else {
                                            T_.error("哟,难道要我用特意功能扫一扫?");
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }


    public void onViewShow(Bundle bundle) {
        super.onViewShow(bundle);

        UpdateBmob.checkUpdate(new UpdateBmob.UpdateListener() {
            @Override
            public void onUpdate(UpdateBmob bmob) {

            }

            @Override
            public void onLast(UpdateBmob bmob) {
                if (mVersionNameView == null) {
                    return;
                }
                mVersionNameView.setText(String.format(Locale.CHINA, "Cv:%s Rv:%s",
                        Device.appVersionName(mActivity), bmob.getVersionName()));
            }
        });

        PasswordBmob.find(new FindListener<PasswordBmob>() {
            @Override
            public void done(List<PasswordBmob> list, BmobException e) {
                if (e == null) {
                    final int size = list.size();
                    mLastBy = "";
                    if (size > 0) {
                        mLastBy = list.get(list.size() - 1).getUsername();
                    }

                    UserBmob.find(new FindListener<UserBmob>() {
                        @Override
                        public void done(List<UserBmob> list, BmobException e) {
                            if (e == null) {
                                int size1 = list.size();
                                mUserName = "";
                                if (size > 0) {
                                    mUserName = list.get(list.size() - 1).getUserName();
                                }

                                if (mDataCountView != null) {
                                    mDataCountView.setText(String.format(Locale.CHINA, "Dc:%s Uc:%s LastBy:%s Un:%s",
                                            RUtils.getShortString(size), RUtils.getShortString(size1),
                                            mLastBy, mUserName));
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
