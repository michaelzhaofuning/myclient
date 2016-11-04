package com.qczb.myclient.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;

/**
 * 2016/8/7
 *
 * @author Michael Zhao
 */
public class ProviderActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        ProviderFragment f = new ProviderFragment();
        Bundle b = new Bundle();
        b.putString("bid", getIntent().getStringExtra("bid"));
        f.setArguments(b);
        getFragmentManager().beginTransaction()
                .add(R.id.container, f).commit();
    }
}
