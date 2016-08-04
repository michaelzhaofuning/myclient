package com.qczb.myclient.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseFragment;

/**
 * 2016/8/3
 *
 * @author Michael Zhao
 */
public class PublishGoodFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.publish_good_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolBar(view, "发布");
        mToolbar.setNavigationIcon(R.mipmap.navigation_icon);

    }
}
