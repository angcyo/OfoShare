package com.angcyo.ofoshare;

import com.angcyo.bmob.RBmob;
import com.angcyo.uiview.RApplication;
import com.angcyo.uiview.Root;

/**
 * Created by angcyo on 2017-04-17.
 */

public class App extends RApplication {
    @Override
    protected void onInit() {
        super.onInit();
        RBmob.init(this);

        Root.APP_FOLDER = "ofoshare";
    }
}
