package com.qczb.myclient.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qczb.myclient.R;
import com.qczb.myclient.adapter.ListItemAdapter;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.MyCallBack;
import com.qczb.myclient.entity.Provider;
import com.qczb.myclient.entity.Stock;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/11/11
 *
 * @author Michael Zhao
 */

public class AddStockActivity extends BaseActivity {
    public Stock item;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        item = (Stock) getIntent().getSerializableExtra("item");

        getFragmentManager().beginTransaction()
                .add(R.id.container, new AddStockActivity.AddStockFragment()).commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }




    public static class AddStockFragment extends ScrollViewFragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            item = ((AddProviderActivity) getActivity()).item;

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        protected void onSendForm() {
            super.onSendForm();

            map.put("Bid", ListItemAdapter.bid);
            map.put("SId", UUID.randomUUID().toString());
            getHttpService().submitProvider(map).enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                @Override
                public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                    success();
                }
            });
        }

        @Override
        protected int getScrollViewContentLayoutId() {
            return R.layout.scroll_view_add_stock;
        }

        @Override
        protected int getTopImageID() {
            return R.mipmap.add_provider;
        }

        @Override
        protected String getTitle() {
            if (item == null) return "新增库存";
            else  return "编辑库存";
        }

        @Override
        protected Class getItemClass() {
            return Stock.class;
        }
    }
}
