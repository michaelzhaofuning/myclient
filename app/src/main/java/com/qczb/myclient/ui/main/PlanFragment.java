package com.qczb.myclient.ui.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.qczb.myclient.R;
import com.qczb.myclient.adapter.ListItemAdapter;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.UserManager;
import com.qczb.myclient.entity.Item;
import com.qczb.myclient.util.ActivityUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/8/3
 *
 * @author Michael Zhao
 */
public class PlanFragment extends ListItemFragment<Item> {

    @Override
    protected Class<?> getTopAddActivity() {
        return AddPlanActivity.class;
    }

    @Override
    protected int getTopImageID() {
        return R.mipmap.plan;
    }

    @Override
    protected String getTitle() {
        return "拜访计划";
    }

    @Override
    public Call<BaseResult> getRetrofitCall() {
        return null;
//        return getHttpService().getPlans(UserManager.getUID(), null, null, null, null);
    }

    @Override
    protected Class<Item> getItemClass() {
        return Item.class;
    }

    @Override
    protected JsonArray getJsonArray(Response<BaseResult> response) {
        return response.body().getData();
    }

    @Override
    protected String getCacheKey() {
        return "plan";
    }

    @Override
    public Item newEntity() {
        return new Item();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ListItemAdapter((BaseActivity) getActivity(), mList, "plan");
    }
}
