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
public class AddCustomerActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        getFragmentManager().beginTransaction()
                .add(R.id.container, new AddCustomerFragment()).commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

    }

    public static class AddCustomerFragment extends ScrollViewFragment {

        @Override
        protected void onSendForm() {

        }

        @Override
        protected int getScrollViewContentLayoutId() {
            return R.layout.scroll_view_add_customer;
        }

        @Override
        protected int getTopImageID() {
            return R.mipmap.add_customer;
        }

        @Override
        protected String getTitle() {
            return "新增客户";
        }
    }
}
