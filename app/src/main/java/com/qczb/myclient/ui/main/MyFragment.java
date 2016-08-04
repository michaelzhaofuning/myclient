package com.qczb.myclient.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseFragment;


/**
 * 2016/5/4
 *
 * @author Michael Zhao
 */
public class MyFragment extends BaseFragment {

    private ImageView avatar;
    private TextView nickname;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolBar(view, "个人中心");
/*        mToolbar.inflateMenu(R.menu.my_fragment_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ActivityUtil.startActivityForResult(getActivity(), SettingsActivity.class);
                return true;
            }
        });*/

    }
}
