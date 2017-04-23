package com.angcyo.ofoshare.uiview;

import android.view.View;
import android.view.animation.Animation;

import com.angcyo.bmob.UserBmob;
import com.angcyo.library.utils.L;
import com.angcyo.ofoshare.R;
import com.angcyo.uiview.base.Item;
import com.angcyo.uiview.base.SingleItem;
import com.angcyo.uiview.base.UIItemUIView;
import com.angcyo.uiview.github.tagcloud.TagCloudView;
import com.angcyo.uiview.github.tagcloud.TextTagsAdapter;
import com.angcyo.uiview.recycler.RBaseViewHolder;
import com.angcyo.uiview.utils.ClipHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by angcyo on 2017-04-23.
 */

public class AboutUIView extends UIItemUIView<SingleItem> {

    private static List<String> users;
    TagCloudView tagCloudView;
    private View shareView;

    public AboutUIView(View shareView) {
        this.shareView = shareView;
    }

//    @Override
//    public int getDefaultBackgroundColor() {
//        return Color.TRANSPARENT;
//    }


    @Override
    protected String getTitleString() {
        return "感谢你们";
    }

    @Override
    public void onViewCreate(View rootView) {
        super.onViewCreate(rootView);
        mBaseRootLayout.setEnableClip(true);

        if (users == null) {
            UserBmob.find(new FindListener<UserBmob>() {
                @Override
                public void done(List<UserBmob> list, BmobException e) {
                    if (e == null) {
                        users = new ArrayList<String>();
                        for (UserBmob user : list) {
                            users.add(user.getUserName());
                        }
                        updateTagCloudView();
                    }
                }
            });
        }
    }

    private void updateTagCloudView() {
        if (tagCloudView == null) {
            return;
        }
        if (users == null) {
            return;
        }
        tagCloudView.setAdapter(new TextTagsAdapter(users));
    }

    @Override
    protected void initOnShowContentLayout() {
        super.initOnShowContentLayout();
        mBaseRootLayout.startEnterClip(shareView, new ClipHelper.OnEndListener() {
            @Override
            public void onEnd() {
                L.e("onEnd() -> ------onEnd");
            }
        });
    }

    @Override
    public boolean canSwipeBackPressed() {
        return mBaseRootLayout.isClipEnd();
    }

    @Override
    protected void onTitleBackListener() {
        mBaseRootLayout.startExitClip(new ClipHelper.OnEndListener() {
            @Override
            public void onEnd() {
                AboutUIView.super.onTitleBackListener();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        onTitleBackListener();
        return false;
    }


    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_about_view;
    }

    @Override
    protected void createItems(List<SingleItem> items) {
        items.add(new SingleItem() {
            @Override
            public void onBindView(RBaseViewHolder holder, int posInData, Item dataBean) {
                tagCloudView = holder.v(R.id.tag_cloud_view);
                updateTagCloudView();
            }
        });
    }

    @Override
    public Animation loadOtherEnterAnimation() {
        return null;
    }

    @Override
    public Animation loadFinishAnimation() {
        return null;
    }

    @Override
    public Animation loadStartAnimation() {
        return null;
    }

    @Override
    public Animation loadOtherExitAnimation() {
        return null;
    }
}
