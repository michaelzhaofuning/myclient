package com.qczb.myclient.ui.main;

import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.qczb.myclient.R;
import com.qczb.myclient.adapter.ListItemAdapter;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.UserManager;
import com.qczb.myclient.entity.Item;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/8/7
 *
 * @author Michael Zhao
 */
public class ProviderFragment extends ListItemFragment<Item> {
    @Override
    protected Class<?> getTopAddActivity() {
        return AddProviderActivity.class;
    }

    @Override
    protected int getTopImageID() {
        return R.mipmap.add_provider;
    }

    @Override
    protected String getTitle() {
        return "供货商";
    }

    @Override
    public Call<BaseResult> getRetrofitCall() {
        return getHttpService().getPlans(UserManager.getUID(), null, null, null, null);
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
        return "provider";
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
