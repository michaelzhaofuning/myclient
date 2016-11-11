package com.qczb.myclient.ui.main;

import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.qczb.myclient.R;
import com.qczb.myclient.adapter.ListItemAdapter;
import com.qczb.myclient.adapter.StockAdapter;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.UserManager;
import com.qczb.myclient.entity.Stock;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/11/11
 *
 * @author Michael Zhao
 */

public class StockFragment extends ListItemFragment<Stock> {
    @Override
    protected Class<?> getTopAddActivity() {
        return AddStockActivity.class;
    }

    @Override
    protected int getTopImageID() {
        return R.mipmap.add_provider;
    }

    @Override
    protected String getTitle() {
        return "库存";
    }

    @Override
    public Call<BaseResult> getRetrofitCall() {
        return getHttpService().getStocks(ListItemAdapter.bid, UserManager.getUID(), "");
    }

    @Override
    protected Class<Stock> getItemClass() {
        return Stock.class;
    }

    @Override
    protected JsonArray getJsonArray(Response<BaseResult> response) {
        return response.body().getData();
    }

    @Override
    protected String getCacheKey() {
        return "stock";
    }

    @Override
    public Stock newEntity() {
        return new Stock();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new StockAdapter((BaseActivity) getActivity(), mList);
    }
}