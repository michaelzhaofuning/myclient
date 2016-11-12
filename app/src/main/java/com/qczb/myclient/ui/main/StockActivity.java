package com.qczb.myclient.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;

/**
 * 2016/11/11
 *
 * @author Michael Zhao
 */

public class StockActivity extends BaseActivity {

    private StockFragment f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        f = new StockFragment();
        Bundle b = new Bundle();
        b.putString("bid", getIntent().getStringExtra("bid"));
        f.setArguments(b);
        getFragmentManager().beginTransaction()
                .add(R.id.container, f).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((StockFragment) f).onRefresh();
    }
}