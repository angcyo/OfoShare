package com.angcyo.ofoshare.uiview;

import android.Manifest;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.angcyo.bmob.PasswordBmob;
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
import com.angcyo.uiview.utils.T_;
import com.angcyo.uiview.utils.TimeUtil;
import com.angcyo.uiview.widget.ExEditText;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by angcyo on 2017-04-17.
 */

public class MainUIView extends BaseItemUIView {
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
                holder.tv(R.id.version_name_view).setText(Device.appVersionName(mActivity));

                final TextInputLayout inputLayout = holder.v(R.id.edit_text_layout);
                final ExEditText exEditText = holder.v(R.id.edit_text);

                holder.v(R.id.add_view).setBackground(SkinHelper.getSkin().getThemeMaskBackgroundRoundSelector(Color.RED));
                holder.v(R.id.find_view).setBackground(SkinHelper.getSkin().getThemeMaskBackgroundRoundSelector(Color.RED));

                holder.v(R.id.add_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (exEditText.isEmpty() || exEditText.length() < 7) {
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
                        if (exEditText.isEmpty() || exEditText.length() < 7) {
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
                                                    exEditText.setText(s);
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
}
