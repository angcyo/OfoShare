package com.angcyo.ofoshare.uiview;

import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.angcyo.ofoshare.R;
import com.angcyo.ofoshare.util.Main;
import com.angcyo.uiview.base.Item;
import com.angcyo.uiview.base.SingleItem;
import com.angcyo.uiview.dialog.UILoading;
import com.angcyo.uiview.model.TitleBarPattern;
import com.angcyo.uiview.recycler.RBaseViewHolder;
import com.angcyo.uiview.skin.SkinHelper;
import com.angcyo.uiview.utils.Device;
import com.angcyo.uiview.widget.ExEditText;

import java.util.List;

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
            public void onBindView(RBaseViewHolder holder, int posInData, Item dataBean) {
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
                            UILoading.show2(mILayout);
                        }
                    }
                });
            }
        });
    }
}
