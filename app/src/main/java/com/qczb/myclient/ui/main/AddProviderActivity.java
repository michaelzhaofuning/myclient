package com.qczb.myclient.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.MyCallBack;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/8/7
 *
 * @author Michael Zhao
 */
public class AddProviderActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        getFragmentManager().beginTransaction()
                .add(R.id.container, new AddProviderFragment()).commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public static class AddProviderFragment extends ScrollViewFragment {

        @Override
        protected void onSendForm() {
            map.put("Bid", "40289f81563a4a9001563a626f6e0002");
            map.put("editModel", "add");
            getHttpService().submitPlan(map).enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                @Override
                public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                    success();
                }
            });
        }

        @Override
        protected int getScrollViewContentLayoutId() {
            return R.layout.scroll_view_add_provider;
        }

        @Override
        protected int getTopImageID() {
            return R.mipmap.add_provider;
        }

        @Override
        protected String getTitle() {
            return "新增供货商";
        }
    }
}
