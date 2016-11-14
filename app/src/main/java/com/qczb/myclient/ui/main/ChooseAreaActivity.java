package com.qczb.myclient.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.qczb.myclient.R;
import com.qczb.myclient.adapter.AreaAdapter;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseListFragment;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.entity.Area;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/11/14
 *
 * @author Michael Zhao
 */

public class ChooseAreaActivity extends BaseActivity {
    public Fragment f;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        f = new ChooseAreaFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.container, f).commit();
    }

    public static class ChooseAreaFragment extends BaseListFragment<BaseResult, Area> {

        @Override
        public Call<BaseResult> getRetrofitCall() {
            return getHttpService().getAreas();
        }

        @Override
        protected Class<Area> getItemClass() {
            return Area.class;
        }

        @Override
        protected JsonArray getJsonArray(Response<BaseResult> response) {
            return response.body().getData();
        }

        @Override
        protected void initActionBar() {
            initToolBar(getView(), "选择区域");
        }

        @Override
        protected String getCacheKey() {
            return null;
        }

        @Override
        public Area newEntity() {
            return new Area();
        }

        @Override
        protected RecyclerView.Adapter getAdapter() {
            return new AreaAdapter((BaseActivity) getActivity(), mList);
        }
    }

}
