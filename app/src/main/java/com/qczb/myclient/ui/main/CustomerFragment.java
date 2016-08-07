package com.qczb.myclient.ui.main;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.JsonArray;
import com.qczb.myclient.R;
import com.qczb.myclient.adapter.CustomerAdapter;
import com.qczb.myclient.adapter.ListItemAdapter;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.UserManager;
import com.qczb.myclient.entity.Customer;
import com.qczb.myclient.entity.Item;
import com.qczb.myclient.util.ActivityUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/8/7
 *
 * @author Michael Zhao
 */
public class CustomerFragment extends ListItemFragment<Customer> {
    @Override
    protected Class<?> getTopAddActivity() {
        return AddCustomerActivity.class;
    }

    @Override
    protected int getTopImageID() {
        return R.mipmap.customer;
    }

    @Override
    protected String getTitle() {
        return "我的客户";
    }

    @Override
    public Call<BaseResult> getRetrofitCall() {
        return getHttpService().getCustomers(UserManager.getUID());
    }

    @Override
    protected Class<Customer> getItemClass() {
        return Customer.class;
    }


    @Override
    protected JsonArray getJsonArray(Response<BaseResult> response) {
        return response.body().getData();
    }

    @Override
    protected String getCacheKey() {
        return null;
    }

    @Override
    public Customer newEntity() {
        return new Customer();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CustomerAdapter((BaseActivity) getActivity(), mList);
    }
}
