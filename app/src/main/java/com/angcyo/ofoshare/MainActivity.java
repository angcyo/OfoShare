package com.angcyo.ofoshare;

import android.Manifest;

import com.angcyo.ofoshare.uiview.RegisterUIView;
import com.angcyo.ofoshare.util.Main;
import com.angcyo.uiview.base.UILayoutActivity;
import com.angcyo.uiview.container.UIParam;
import com.angcyo.uiview.skin.SkinHelper;

public class MainActivity extends UILayoutActivity {

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

        } else {
            mLayout.replaceIView(new RegisterUIView(), new UIParam(false));
        }
    }
}
