package com.qczb.myclient.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.entity.Provider;
import com.qczb.myclient.entity.Stock;
import com.qczb.myclient.ui.main.AddProviderActivity;
import com.qczb.myclient.ui.main.AddStockActivity;
import com.qczb.myclient.util.ActivityUtil;

import java.util.List;

/**
 * 2016/11/11
 *
 * @author Michael Zhao
 */
public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> implements View.OnClickListener{
    private List<Stock> mList;
    private BaseActivity mActivity;
    public StockAdapter(BaseActivity activity, List<Stock> list) {
        mList = list;
        mActivity = activity;
    }

    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StockAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent, false));
    }

    @Override
    public void onBindViewHolder(StockAdapter.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);
        holder.title.setText(mList.get(position).getDName());
        holder.date.setText(mList.get(position).getAddTime());
        holder.location.setText(mList.get(position).getBName());
        holder.status.setText(mList.get(position).getStockNums()+"");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        int p = (int) v.getTag();

        Bundle b = new Bundle();
        b.putSerializable("item", mList.get(p));
        ActivityUtil.startActivityForResult(mActivity, AddStockActivity.class, b, 100);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, location, date, status;
        ImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            location = (TextView) itemView.findViewById(R.id.location);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (TextView) itemView.findViewById(R.id.status);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}