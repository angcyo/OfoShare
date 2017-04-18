package com.angcyo.ofoshare.uiview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.angcyo.ofoshare.R;
import com.angcyo.uiview.base.UIContentView;
import com.angcyo.uiview.github.utilcode.utils.VibrationUtils;
import com.angcyo.uiview.model.TitleBarPattern;
import com.angcyo.uiview.utils.T_;
import com.angcyo.uiview.widget.RImageCheckView;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：扫一扫界面
 * 创建人员：Robi
 * 创建时间：2017/01/17 09:19
 * 修改人员：Robi
 * 修改时间：2017/01/17 09:19
 * 修改备注：
 * Version: 1.0.0
 */
public class ScanUIView extends UIContentView implements QRCodeView.Delegate {
    ZXingView mZxingView;
    RImageCheckView mLightSwitchView;
    RImageCheckView mPhotoSelectorView;

    @Override
    public boolean canTryCaptureView() {
        return true;//不支持滑动删除
    }

    @Override
    protected void inflateContentLayout(RelativeLayout baseContentLayout, LayoutInflater inflater) {
        inflate(R.layout.view_scan_layout);
    }

    @Override
    protected void initOnShowContentLayout() {
        super.initOnShowContentLayout();
        mZxingView = mViewHolder.v(R.id.zxing_view);
        mLightSwitchView = mViewHolder.v(R.id.light_switch_view);
        mLightSwitchView.setOnCheckedChangeListener(new RImageCheckView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RImageCheckView buttonView, boolean isChecked) {
                if (isChecked) {
                    mZxingView.openFlashlight();
                } else {
                    mZxingView.closeFlashlight();
                }
            }
        });
    }

    @Override
    protected TitleBarPattern getTitleBar() {
        return super.getTitleBar()
                .setTitleString("")
                .setShowBackImageView(true)
                .setTitleBarBGColor(Color.TRANSPARENT)
                .setFloating(true);
    }

    @Override
    public void onViewShow(Bundle bundle) {
        super.onViewShow(bundle);
        mZxingView.startCamera();
        mZxingView.showScanRect();
        mZxingView.startSpotDelay(160);
//        mZxingView.startSpotAndShowRect();
    }

    @Override
    public void onViewHide() {
        super.onViewHide();
        mZxingView.stopCamera();
    }

    @Override
    public void onViewLoad() {
        super.onViewLoad();
        mZxingView.setDelegate(this);
    }

    @Override
    public void onViewUnload() {
        super.onViewUnload();
        mZxingView.onDestroy();
    }

    @Override
    public void onViewShowFirst(Bundle bundle) {
        super.onViewShowFirst(bundle);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        T_.show(result);
        VibrationUtils.vibrate(mActivity, 200);//震动
        mZxingView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
    }
}
