package com.angcyo.ofoshare;

import android.content.Context;
import android.graphics.Color;

import com.angcyo.uiview.skin.BaseSkin;

/**
 * Created by angcyo on 2017-04-17.
 */

public class MainSkin extends BaseSkin {
    public MainSkin(Context context) {
        super(context);
    }

    @Override
    public int getThemeColor() {
        return Color.parseColor("#F8D501");
    }

    @Override
    public int getThemeSubColor() {
        return Color.parseColor("#F8D501");
    }

    @Override
    public int getThemeDarkColor() {
        return Color.parseColor("#80F8D501");
    }
}
