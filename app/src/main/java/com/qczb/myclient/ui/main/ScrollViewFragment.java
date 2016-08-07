package com.qczb.myclient.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseFragment;

/**
 * 2016/8/4
 *
 * @author Michael Zhao
 */
public abstract class ScrollViewFragment extends BaseFragment {
    ImageView topImageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coordinator_fragment, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        topImageView = (ImageView) v.findViewById(R.id.top_image);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getTitle());
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.navigation_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        NestedScrollView nestedScrollView = (NestedScrollView) v.findViewById(R.id.scroll_view);
        nestedScrollView.addView(getActivity().getLayoutInflater().inflate(getScrollViewContentLayoutId(), null));

        topImageView.setImageDrawable(getResources().getDrawable(getTopImageID()));

        onSendForm();

    }

    protected abstract void onSendForm();

    protected abstract int getScrollViewContentLayoutId();

    protected abstract int getTopImageID();

    protected abstract String getTitle();
}
