package com.qczb.myclient.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.ui.publish.SuccessActivity;
import com.qczb.myclient.util.ActivityUtil;

/**
 * 2016/8/7
 *
 * @author Michael Zhao
 */
public class AddPlanActivity extends BaseActivity {
    public static int CODE = 41;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        getFragmentManager().beginTransaction()
                .add(R.id.container, new AddPlanFragment()).commit();

    }

    public void commit(View view) {
        finish();
        ActivityUtil.startActivityForResult(this, SuccessActivity.class);
    }

    public static class AddPlanFragment extends ScrollViewFragment {

        @Override
        protected void onSendForm() {

        }

        @Override
        protected int getScrollViewContentLayoutId() {
            return R.layout.scroll_view_add_plan;
        }

        @Override
        protected int getTopImageID() {
            return R.mipmap.add_plan;
        }

        @Override
        protected String getTitle() {
            return "新增拜访";
        }
    }
}
