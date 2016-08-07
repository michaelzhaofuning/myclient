package com.qczb.myclient.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.photoselector.model.PhotoModel;
import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseEntity;
import com.qczb.myclient.util.ActivityUtil;
import com.qczb.myclient.view.PhotoPopupWindow;

import java.util.ArrayList;

/**
 * 2016/8/7
 *
 * @author Michael Zhao
 */
public class StartPlanActivity extends BaseActivity {
    private ArrayList<PhotoModel> photoModels = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        getFragmentManager().beginTransaction()
                .add(R.id.container, new StartPlanFragment()).commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public void providers(View view) {
        ActivityUtil.startActivityForResult(this, ProviderActivity.class);
    }

    public void shot(View view) {
        new PhotoPopupWindow(this).show(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoPopupWindow.callBack(this, photoModels, requestCode, resultCode, data, null, StartPlanFragment.mContainer);
    }

    public static class StartPlanFragment extends ScrollViewFragment {
        static LinearLayout mContainer;

        @Override
        public void onViewCreated(View v, Bundle savedInstanceState) {
            super.onViewCreated(v, savedInstanceState);
            mContainer = (LinearLayout) v.findViewById(R.id.container_photos);
        }

        @Override
        protected void onSendForm() {

        }

        @Override
        protected int getScrollViewContentLayoutId() {
            return R.layout.scroll_view_start_plan;
        }

        @Override
        protected int getTopImageID() {
            return R.mipmap.start_visit;
        }

        @Override
        protected String getTitle() {
            return "开始拜访";
        }
    }
}
