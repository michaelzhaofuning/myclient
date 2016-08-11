package com.qczb.myclient.ui.main;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.util.ActivityUtil;

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
        public void onViewCreated(View v, Bundle savedInstanceState) {
            super.onViewCreated(v, savedInstanceState);
            final LinearLayout weddingFeast = (LinearLayout) v.findViewById(R.id.wedding_feast);
            Switch s = (Switch) v.findViewById(R.id.switch_wedding_feast);
            s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        ObjectAnimator.ofFloat(weddingFeast, View.SCALE_Y, 0, 1).start();
                    else
                        ObjectAnimator.ofFloat(weddingFeast, View.SCALE_Y, 1, 0).start();
                }
            });



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
