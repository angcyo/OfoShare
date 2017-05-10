package com.angcyo.ofoshare.uiview;

import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.angcyo.bmob.PasswordBmob;
import com.angcyo.ofoshare.R;
import com.angcyo.ofoshare.util.Main;
import com.angcyo.uiview.base.UIIDialogImpl;
import com.angcyo.uiview.dialog.UILoading;
import com.angcyo.uiview.skin.SkinHelper;
import com.angcyo.uiview.utils.T_;
import com.angcyo.uiview.widget.ExEditText;
import com.angcyo.uiview.widget.RSoftInputLayout;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2017/04/18 10:53
 * 修改人员：Robi
 * 修改时间：2017/04/18 10:53
 * 修改备注：
 * Version: 1.0.0
 */
public class AddDialog extends UIIDialogImpl {

    String number;

    public AddDialog(String number) {
        this.number = number;
    }

    @Override
    protected View inflateDialogView(RelativeLayout dialogRootLayout, LayoutInflater inflater) {
        return inflate(R.layout.view_add_dialog);
    }

    @Override
    public int getGravity() {
        return Gravity.TOP;
    }

    @Override
    protected void initDialogContentView() {
        super.initDialogContentView();
        mDialogContentRootLayout.setBackgroundColor(getColor(R.color.base_orange));

        final TextInputLayout inputLayout = mViewHolder.v(R.id.edit_text_layout);
        final ExEditText exEditText = mViewHolder.v(R.id.edit_text);
        String hint = "小黄车 " + number + " 的密码:";
        //exEditText.setHint(hint);
        inputLayout.setHint(hint);

        RSoftInputLayout.showSoftInput(exEditText);

        mViewHolder.v(R.id.save_view).setBackground(SkinHelper.getSkin().getThemeMaskBackgroundRoundSelector(Color.RED));
        mViewHolder.v(R.id.save_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exEditText.isEmpty() || exEditText.length() < 4) {
                    inputLayout.setError("这是哪个国家的密码?");
                    exEditText.requestFocus();
                } else {
                    UILoading.show2(mILayout);
                    PasswordBmob.upload(new PasswordBmob(exEditText.string(), number, Main.userName()), new SaveListener<String>() {

                        @Override
                        public void done(String passwordBmob, BmobException e) {
                            if (e == null) {
                                T_.show("感谢分享, 你是个好孩子!");
                            }
                            UILoading.hide();
                            finishDialog();
                        }
                    });
                }
            }
        });
    }
}
