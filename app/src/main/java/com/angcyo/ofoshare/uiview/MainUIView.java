package com.angcyo.ofoshare.uiview;

import com.angcyo.ofoshare.util.Main;
import com.angcyo.uiview.base.SingleItem;
import com.angcyo.uiview.model.TitleBarPattern;
import com.angcyo.uiview.skin.SkinHelper;

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
    protected void createItems(List<SingleItem> items) {

    }
}
