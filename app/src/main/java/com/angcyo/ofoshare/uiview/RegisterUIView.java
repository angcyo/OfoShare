package com.angcyo.ofoshare.uiview;

import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.TextView;

import com.angcyo.bmob.UserBmob;
import com.angcyo.ofoshare.R;
import com.angcyo.ofoshare.util.Main;
import com.angcyo.uiview.base.Item;
import com.angcyo.uiview.base.SingleItem;
import com.angcyo.uiview.dialog.UILoading;
import com.angcyo.uiview.model.TitleBarPattern;
import com.angcyo.uiview.recycler.RBaseViewHolder;
import com.angcyo.uiview.skin.SkinHelper;
import com.angcyo.uiview.utils.T_;
import com.angcyo.uiview.widget.ExEditText;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by angcyo on 2017-04-17.
 */

public class RegisterUIView extends BaseItemUIView {

    @Override
    protected TitleBarPattern getTitleBar() {
        return null;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.view_register;
    }

    @Override
    protected void createItems(List<SingleItem> items) {
        items.add(new SingleItem() {
            @Override
            public void onBindView(RBaseViewHolder holder, int posInData, Item dataBean) {
                holder.itemView.setBackgroundColor(SkinHelper.getSkin().getThemeColor());
//                holder.itemView.setBackgroundColor(Color.parseColor("#8D8A65"));
                final ExEditText exEditText = holder.v(R.id.edit_text);
                final TextInputLayout inputLayout = holder.v(R.id.edit_text_layout);
                TextView textView = holder.v(R.id.text_view);
                textView.setBackground(SkinHelper.getSkin().getThemeMaskBackgroundRoundSelector(Color.RED));
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (exEditText.isEmpty()) {
                            exEditText.requestFocus();
                            inputLayout.setError("啥玩意啊?");
                        } else {
                            UILoading.show2(mILayout);

                            UserBmob.isUserExist(exEditText.string(), new FindListener<UserBmob>() {
                                @Override
                                public void done(List<UserBmob> list, BmobException e) {
                                    if (e == null) {
                                        if (list.size() > 0) {
                                            exEditText.requestFocus();
                                            inputLayout.setError("这玩意还能重名? 换个呗");
                                        } else {
                                            UserBmob.upload(exEditText.string(), new SaveListener<String>() {

                                                @Override
                                                public void done(String s, BmobException e) {
                                                    if (e == null) {
                                                        //用户创建成功
                                                        T_.show("欢迎:" + exEditText.string() + "加入Ofo密码共享家庭.");
                                                        Main.register(exEditText.string());
                                                        replaceIView(new MainUIView());
                                                    } else {
                                                        T_.show(e.getMessage());
                                                    }
                                                    UILoading.hide();
                                                }
                                            });
                                        }
                                    } else {
                                        //查询失败
                                        T_.show(e.getMessage());
                                        UILoading.hide();
                                    }
                                }
                            });


                        }
                    }
                });

            }
        });
    }
}
