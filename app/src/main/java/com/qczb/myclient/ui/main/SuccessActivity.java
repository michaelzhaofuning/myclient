package com.qczb.myclient.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;


/**
 * 2016/5/30
 *
 * @author Michael Zhao
 */
public class SuccessActivity extends BaseActivity {
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        setContentView(R.layout.success_activity);
        TextView textView = (TextView) findViewById(R.id.desc);
        assert textView != null;
        textView.setText(title != null ? title : "发布成功");
        initToolBar(title != null ? title : "发布成功");

        mToolbar.inflateMenu(R.menu.confirm_button);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return true;
            }
        });


    }
}
