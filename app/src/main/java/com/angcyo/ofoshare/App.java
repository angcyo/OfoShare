package com.angcyo.ofoshare;

import com.angcyo.bmob.RBmob;
import com.angcyo.uiview.RApplication;

/**
 * Created by angcyo on 2017-04-17.
 */

public class App extends RApplication {
    @Override
    protected void onInit() {
        super.onInit();
        RBmob.init(this);
    }
}
